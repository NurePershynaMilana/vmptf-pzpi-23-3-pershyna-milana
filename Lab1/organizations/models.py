from django.db import models


# --- Рівень 1 ---

class Organization(models.Model):
    name = models.CharField(max_length=255)
    description = models.TextField(blank=True)

    def __str__(self):
        return self.name


class Resource(models.Model):
    organization = models.ForeignKey(Organization, on_delete=models.CASCADE, related_name='resources')
    name = models.CharField(max_length=255)
    quantity = models.PositiveIntegerField(default=0)

    def __str__(self):
        return f"{self.name} ({self.organization.name})"


class Volunteer(models.Model):
    organization = models.ForeignKey(Organization, on_delete=models.CASCADE, related_name='volunteers')
    name = models.CharField(max_length=255)

    def __str__(self):
        return self.name


class Project(models.Model):
    organization = models.ForeignKey(Organization, on_delete=models.CASCADE, related_name='projects')
    title = models.CharField(max_length=255)
    description = models.TextField(blank=True)

    def __str__(self):
        return self.title


# --- Рівень 2 ---

class Donation(models.Model):
    organization = models.ForeignKey(Organization, on_delete=models.CASCADE, related_name='donations')
    title = models.CharField(max_length=255)
    goal_amount = models.DecimalField(max_digits=10, decimal_places=2)
    collected_amount = models.DecimalField(max_digits=10, decimal_places=2, default=0)
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return self.title