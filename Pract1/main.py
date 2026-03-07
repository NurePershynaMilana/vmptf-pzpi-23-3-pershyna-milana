# Рівень 1, Завдання 1: Створіть програму, яка виводить на екран від 1 до 10
for i in range(1, 11):
    print(i)


# Рівень 2, Завдання 1: Напишіть програму, яка знаходить середнє значення з трьох чисел, введених користувачем
a = float(input("Enter first number: "))
b = float(input("Enter second number: "))
c = float(input("Enter third number: "))
average = (a + b + c) / 3
print(f"Average: {average}")


# Рівень 3, Завдання 1: Реалізуйте програму, яка приймає на вхід рік народження користувача та виводить його вік
birth_year = int(input("Enter your birth year: "))
current_year = 2026
age = current_year - birth_year
print(f"Your age: {age}")


# Рівень 4, Завдання 1: Напишіть клас "Книга" з властивостями, такими як назва, автор та рік видання.
# Створіть об'єкт цього класу та виведіть його характеристики
class Book:
    def __init__(self, title, author, year):
        self.title = title
        self.author = author
        self.year = year

    def display_info(self):
        print(f"Title: {self.title}")
        print(f"Author: {self.author}")
        print(f"Year: {self.year}")

my_book = Book(title="The Pragmatic Programmer", author="Andrew Hunt", year=1999)
my_book.display_info()