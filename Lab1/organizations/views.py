from django.shortcuts import render, get_object_or_404, redirect
from .models import Organization, Donation
from .forms import DonationForm, DonationFilterForm


# --- Рівень 1 ---

def org_list(request):
    organizations = Organization.objects.prefetch_related(
        'resources', 'volunteers', 'projects'
    ).all()
    return render(request, 'organizations/org_list.html', {'organizations': organizations})


def org_detail(request, pk):
    org = get_object_or_404(Organization, pk=pk)
    return render(request, 'organizations/org_detail.html', {'org': org})


# --- Рівень 2 ---

def donation_create(request, org_pk):
    org = get_object_or_404(Organization, pk=org_pk)
    if request.method == 'POST':
        form = DonationForm(request.POST)
        if form.is_valid():
            donation = form.save(commit=False)
            donation.organization = org
            donation.save()
            return redirect('org_detail', pk=org.pk)
    else:
        form = DonationForm()
    return render(request, 'organizations/donation_form.html', {'form': form, 'org': org})


def donation_edit(request, pk):
    donation = get_object_or_404(Donation, pk=pk)
    if request.method == 'POST':
        form = DonationForm(request.POST, instance=donation)
        if form.is_valid():
            form.save()
            return redirect('org_detail', pk=donation.organization.pk)
    else:
        form = DonationForm(instance=donation)
    return render(request, 'organizations/donation_form.html', {'form': form, 'org': donation.organization})


def donation_delete(request, pk):
    donation = get_object_or_404(Donation, pk=pk)
    org_pk = donation.organization.pk
    if request.method == 'POST':
        donation.delete()
        return redirect('org_detail', pk=org_pk)
    return render(request, 'organizations/donation_confirm_delete.html', {'donation': donation})


# --- Рівень 3 ---

def donation_list(request, org_pk):
    org = get_object_or_404(Organization, pk=org_pk)
    donations = Donation.objects.filter(organization=org)
    form = DonationFilterForm(request.GET)

    if form.is_valid():
        search = form.cleaned_data.get('search')
        sort = form.cleaned_data.get('sort')
        if search:
            donations = donations.filter(title__icontains=search)
        if sort:
            donations = donations.order_by(sort)

    return render(request, 'organizations/donation_list.html', {
        'org': org,
        'donations': donations,
        'form': form,
    })