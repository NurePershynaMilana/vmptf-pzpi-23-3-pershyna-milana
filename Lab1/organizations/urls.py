from django.urls import path
from . import views

urlpatterns = [
    # Рівень 1
    path('', views.org_list, name='org_list'),
    path('<int:pk>/', views.org_detail, name='org_detail'),

    # Рівень 2
    path('<int:org_pk>/donations/add/', views.donation_create, name='donation_create'),
    path('donations/<int:pk>/edit/', views.donation_edit, name='donation_edit'),
    path('donations/<int:pk>/delete/', views.donation_delete, name='donation_delete'),

    # Рівень 3
    path('<int:org_pk>/donations/', views.donation_list, name='donation_list'),
]