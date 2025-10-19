create database hospital_mysql;
CREATE SCHEMA hospital;

CREATE TYPE hospital.datos_contacto AS (
    nombre_contacto VARCHAR(255),
    nif VARCHAR(50),
    telefono INT,
    email VARCHAR(250)
);


CREATE TABLE IF NOT EXISTS hospital.especialidades
(
    id_especialidad serial,
    nombre_especialidad character varying(100),
    PRIMARY KEY (id_especialidad)
);

CREATE TABLE IF NOT EXISTS hospital.medicos
(
    id_medico serial,
    nombre_medico character varying(100),
	contacto hospital.datos_contacto,
    PRIMARY KEY (id_medico)
);

CREATE TABLE IF NOT EXISTS hospital.salas
(
    id_sala serial,
    nombre_sala character varying(100),
    ubicacion character varying(100),
    PRIMARY KEY (id_sala)
);

CREATE TABLE IF NOT EXISTS hospital.tratamientos
(
    id_tratamiento integer,	
    id_medico integer,
    id_especialidad integer,
	 FOREIGN KEY (id_medico) REFERENCES hospital.medicos(id_medico)
		ON DELETE CASCADE ON UPDATE CASCADE,    
    FOREIGN  KEY (id_especialidad) REFERENCES hospital.especialidades(id_especialidad)
		ON DELETE CASCADE ON UPDATE CASCADE,  
    PRIMARY KEY (id_tratamiento)
);

CREATE TABLE IF NOT EXISTS hospital.salas_tratamientos
(    
    id_sala integer NOT NULL,
    id_tratamiento integer NOT NULL,
    PRIMARY KEY (id_sala, id_tratamiento),
    FOREIGN KEY (id_sala) REFERENCES hospital.salas(id_sala)
		ON DELETE CASCADE ON UPDATE CASCADE,    
    FOREIGN  KEY (id_tratamiento) REFERENCES hospital.tratamientos(id_tratamiento)
		ON DELETE CASCADE ON UPDATE CASCADE    
);

INSERT INTO hospital.especialidades(nombre_espacialidad) VALUES
('Dermatología'),
('Cirugía Estética'),
('Oncología'),
('Nefrología'),
('Cirugía General'),
('Infectología'),
('Endocrinología');

INSERT INTO hospital.medicos(nombre_medico, contacto) VALUES
('Dra. Laura Méndez', ROW('Isabel Pérez', '12345678A', 612345678, 'laura.mendez@hospital.org')),
('Dr. Carlos Ruiz', ROW('Antonio Gómez', '87654321B', 698765432, 'carlos.ruiz@hospital.org')),
('Dra. Marta Sanz', ROW('Lucía Martínez', '11223344C', 611122233, 'marta.sanz@hospital.org')),
('Dr. Joaquín Herrera', ROW('Elena Ramírez', '55667788D', 699988877, 'joaquin.herrera@hospital.org')),
('Dra. Paula López', ROW('David Morales', '99887766E', 622334455, 'paula.lopez@hospital.org')),
('Dr. Pedro Martín', ROW('Sandra Ortega', '44556677F', 600123456, 'pedro.martin@hospital.org')),
('Dra. Nuria Torres', ROW('Miguel Pérez', '33445566G', 634567890, 'nuria.torres@hospital.org'));

INSERT INTO hospital.salas(nombre_sala, ubicacion) VALUES
('Sala Dermatología 1', 'Planta 1 - Ala A'),
('Sala Cirugía Estética', 'Planta 2 - Ala B'),
('Sala Oncología', 'Planta 3 - Ala C'),
('Sala Diálisis', 'Planta Baja - Ala D'),
('Sala Cirugía General', 'Planta 2 - Ala A'),
('Sala Infecciosas', 'Planta 4 - Ala E'),
('Sala Reproducción', 'Planta 1 - Ala B');

-- id_tratamiento | id_medico | id_especialidad
INSERT INTO hospital.tratamientos(id_tratamiento, id_medico, id_especialidad) VALUES
(1, 1, 1),  -- Peeling - Dermatología
(2, 2, 2),  -- Rinoplastia - Cirugía Estética
(3, 3, 3),  -- Radioterapia - Oncología
(4, 4, 4),  -- Diálisis - Nefrología
(5, 5, 5),  -- Cirugía menor - Cirugía General
(6, 3, 3),  -- Quimioterapia - Oncología
(7, 6, 6),  -- Inmunoterapia - Infectología
(8, 7, 7);  -- Terapia hormonal - Endocrinología

-- id_sala | id_tratamiento
INSERT INTO hospital.salas_tratamientos(id_sala, id_tratamiento) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(3, 6),  -- Quimioterapia también en oncología
(6, 7),
(7, 8);


