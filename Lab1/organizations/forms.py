from django import forms
from .models import Donation


# --- Рівень 2 ---

class DonationForm(forms.ModelForm):
    class Meta:
        model = Donation
        fields = ['title', 'goal_amount', 'collected_amount']


# --- Рівень 3 ---

class DonationFilterForm(forms.Form):
    search = forms.CharField(required=False, label='Пошук')
    sort = forms.ChoiceField(
        required=False,
        choices=[
            ('created_at', 'За датою'),
            ('goal_amount', 'За метою'),
            ('collected_amount', 'За зібраним'),
        ],
        label='Сортування'
    )