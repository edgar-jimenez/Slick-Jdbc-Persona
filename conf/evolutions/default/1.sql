# User schema

# --- !Ups
create table `persona` (`name` VARCHAR(30) NOT NULL PRIMARY KEY,`apellido` VARCHAR(30) NOT NULL,`edad` INT(5)
NOT NULL)

# --- !Downs
drop table `persona`

