create database if not exists ultimap;
create table car
(
    id          int          not null
        primary key,
    consumption double       null,
    fueltype    int          null,
    name        varchar(255) null
);

# 25 most popular car models (https://de.statista.com/statistik/daten/studie/3149/umfrage/automodelle-mit-den-meisten-neuzulassungen-in-deutschland/)
# Details from Wikipedia
INSERT INTO ultimap.car (id, consumption, fueltype, name) VALUES (1, 4.8, 0, 'VW Golf 8 2.0 TDI');
INSERT INTO ultimap.car (id, consumption, fueltype, name) VALUES (2, 7.2, 1, 'VW Passat B8 2.0 TSI');
INSERT INTO ultimap.car (id, consumption, fueltype, name) VALUES (3, 5, 0, 'VW Tiguan II 2.0 TDI SCR');
INSERT INTO ultimap.car (id, consumption, fueltype, name) VALUES (4, 5.3, 1, 'Ford Focus ''18 1.0 EcoBoost');
INSERT INTO ultimap.car (id, consumption, fueltype, name) VALUES (5, 4, 0, 'Skoda Octavia IV 2.0 TDI');
INSERT INTO ultimap.car (id, consumption, fueltype, name) VALUES (6, 4.6, 1, 'Opel Corsa F 1.2');
INSERT INTO ultimap.car (id, consumption, fueltype, name) VALUES (7, 3.3, 0, 'Opel Corsa F 1.5 Diesel');
INSERT INTO ultimap.car (id, consumption, fueltype, name) VALUES (8, 4.7, 0, 'VW T-Roc A1 2.0 TDI');
INSERT INTO ultimap.car (id, consumption, fueltype, name) VALUES (9, 4.5, 0, 'BMW "3er" G20 320d xDrive');
INSERT INTO ultimap.car (id, consumption, fueltype, name) VALUES (10, 3.9, 0, 'VW Polo VI 1.6 TDI');
INSERT INTO ultimap.car (id, consumption, fueltype, name) VALUES (11, 6.4, 0, 'Fiat Ducato 115-Multijet');
INSERT INTO ultimap.car (id, consumption, fueltype, name) VALUES (12, 6.5, 1, 'Mini Cooper S Countryman 2. Generation');
INSERT INTO ultimap.car (id, consumption, fueltype, name) VALUES (13, 4.5, 0, 'Mercedes "A-Klasse" Baureihe 177 A 200 d');
INSERT INTO ultimap.car (id, consumption, fueltype, name) VALUES (14, 5.8, 0, 'Mercedes "GLC" X 253 300 d');
INSERT INTO ultimap.car (id, consumption, fueltype, name) VALUES (15, 6.2, 1, 'Mercedes "C-Klasse" Baureihe 205 C 200');
INSERT INTO ultimap.car (id, consumption, fueltype, name) VALUES (16, 3.9, 0, 'Seat Leon IV TDI');
INSERT INTO ultimap.car (id, consumption, fueltype, name) VALUES (17, 7.2, 1, 'Audi A4 35 TFSI');
INSERT INTO ultimap.car (id, consumption, fueltype, name) VALUES (18, 4.6, 1, 'Ford Fiesta ''18 1.1');
INSERT INTO ultimap.car (id, consumption, fueltype, name) VALUES (19, 5.8, 1, 'Audi A3 8Y 35 TFSI');
INSERT INTO ultimap.car (id, consumption, fueltype, name) VALUES (20, 6.6, 1, 'Mercedes "E-Klasse" Baureihe 238 E 200');
INSERT INTO ultimap.car (id, consumption, fueltype, name) VALUES (21, 4.8, 0, 'BMW "1er" F40 120d xDrive');
INSERT INTO ultimap.car (id, consumption, fueltype, name) VALUES (22, 4.3, 0, 'Hyundai Kona 1.6 CRDi');
INSERT INTO ultimap.car (id, consumption, fueltype, name) VALUES (23, 7.5, 1, 'Audi A6 C8 45 TFSI');
INSERT INTO ultimap.car (id, consumption, fueltype, name) VALUES (24, 4.6, 0, 'Opel Astra K 1.5 Diesel');