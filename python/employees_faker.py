from faker import Faker
from faker import Factory
from random import *
fakerFR = Factory.create('fr_FR')
faker_en = Faker()

print("nom;prenom;matricule;date_de_naissance;email;telephone;rue;ville;cp;genre")
for i in range(1000):
    nom = fakerFR.last_name()
    prenom = fakerFR.first_name()
    phone = fakerFR.phone_number()
    matricule = str(fakerFR.random_number(5))
    date_de_naissance = str(fakerFR.date_of_birth(tzinfo=None, minimum_age=22, maximum_age=60))
    email = (nom[0]+"."+prenom+"@onepoint.test").lower()
    address = fakerFR.address().replace("\n", "|")
    address = address.split("|")
    zip = address[1].split(" ")[0]
    city = address[1].split(" ")[1]
    street = address[0].replace (",", "")
    gendre = str(randint(1,2))
    print( ";".join((nom, prenom, matricule, date_de_naissance, email, phone, street, city, zip, gendre)))