from django.contrib import admin
from .models import Organization, Resource, Volunteer, Project, Donation

admin.site.register(Organization)
admin.site.register(Resource)
admin.site.register(Volunteer)
admin.site.register(Project)
admin.site.register(Donation)