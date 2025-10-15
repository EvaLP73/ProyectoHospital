drop database if exists hospital_mysql;
create database hospital_mysql;

use hospital_mysql;

create table pacientes (
	id_paciente int primary key auto_increment,	
    nombre varchar(100) not null,
    email varchar(100) not null,
    fecha_nacimiento date
);

create table citas (
	id_cita int primary key auto_increment,
    id_paciente int,
    fecha date,
    constraint fk_paciente foreign key (id_paciente) references pacientes(id_paciente)   
);

create table tratamientos (
    id_tratamiento int primary key auto_increment,
	nombre_tratamiento varchar(100) not null,      
    descripcion text not null
);

insert into pacientes(nombre, email, fecha_nacimiento) values ('Roberto Mazos', 'agudi@gmail.com', '2003-12-17' );
insert into pacientes(nombre, email, fecha_nacimiento) values ('Cristian Lopez', 'lopez@gmail.com', '1973-08-25');
insert into pacientes(nombre, email, fecha_nacimiento) values ('Sara Carbonero', 'sara@gmail.com', '1985-10-07' );
insert into pacientes(nombre, email, fecha_nacimiento) values ('Patricia Ballester', 'patri@gmail.com', '1995-07-02' );

insert into citas(id_paciente, fecha) values (1, '2024-08-15');
insert into citas(id_paciente, fecha) values (2,  '2023-12-10');
insert into citas(id_paciente, fecha) values (3, '2022-02-8');
insert into citas(id_paciente, fecha) values (4, '2024-04-13');
insert into citas(id_paciente, fecha) values (1, '2023-02-22');

insert into tratamientos(nombre_tratamiento, descripcion) values ('Peeling', 'Estiramiento facial');
insert into tratamientos(nombre_tratamiento, descripcion) values ('Rinoplastia', 'estetica perfil nariz');
insert into tratamientos(nombre_tratamiento, descripcion) values ('Radioterapia', 'tratamiento cancer con rayos x');
insert into tratamientos(nombre_tratamiento, descripcion) values ('Dialisis', 'Tratamiento riñones');
insert into tratamientos(nombre_tratamiento, descripcion) values ('Cirugia menor', 'extracción lunar');
insert into tratamientos(nombre_tratamiento, descripcion) values ('Quimioterapia', 'tratamiento cancer con quimio');
insert into tratamientos(nombre_tratamiento, descripcion) values ('Inmunoterapia', 'tratamiento contra VIH');
insert into tratamientos(nombre_tratamiento, descripcion) values ('Terapia hormonal', 'Tratamiento para reproduccion asistida');

create table pacientes_tratamientos(
	id_paciente int,
    id_tratamiento int,
    fecha_inicio date,
    primary key (id_paciente, id_tratamiento),
    constraint fk_pt_paciente foreign key (id_paciente) references pacientes(id_paciente),
    constraint fk_tratamiento foreign key (id_tratamiento) references tratamientos(id_tratamiento)
);

insert into pacientes_tratamientos values (1, 1, '2024-08-12');
insert into pacientes_tratamientos values (2, 4, '2025-07-11');
insert into pacientes_tratamientos values (2, 3, '2023-12-24');
insert into pacientes_tratamientos values (3, 2, '2022-10-02');
insert into pacientes_tratamientos values (4, 5, '2025-11-08');
insert into pacientes_tratamientos values (1, 6, '2025-05-29');
insert into pacientes_tratamientos values (2, 7, '2024-02-19');

select * from tratamientos;