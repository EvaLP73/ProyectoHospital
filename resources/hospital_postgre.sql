CREATE TYPE datos_contacto AS (
    nombre_contacto VARCHAR(255),
    nif VARCHAR(50),
    telefono INT,
    email VARCHAR(250)
);


CREATE TABLE IF NOT EXISTS hospital.especialidades
(
    id_especialidad serial,
    nombre_espacialidad character varying(100),
    PRIMARY KEY (id_especialidad)
);

CREATE TABLE IF NOT EXISTS hospital.medicos
(
    id_medico serial,
    nombre_medico character varying(100),
	contacto datos_contacto,
    PRIMARY KEY (id_medico)
);

CREATE TABLE IF NOT EXISTS hospital.salas
(
    id_sala serial,
    nombre_sala character varying(100),
    ubicacion character varying(100),
    PRIMARY KEY (id_sala)
);

CREATE TABLE IF NOT EXISTS hospital.salas_tratamientos
(
    id_sala integer,
    id_tratamiento integer,
    PRIMARY KEY (id_sala, id_tratamiento)
);

CREATE TABLE IF NOT EXISTS hospital.tratamientos
(
    id_tratamiento integer,	
    id_medico integer REFERENCES medicos(id_medico),
    id_especialidad integer REFERENCES especialidades(id_especialidad),
    PRIMARY KEY (id_tratamiento)
);