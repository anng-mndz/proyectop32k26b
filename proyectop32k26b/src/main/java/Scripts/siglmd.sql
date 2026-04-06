/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 * Author:  Soporte
 * Created: 20/03/2026
 */

CREATE DATABASE IF NOT EXISTS sig;
USE sig;

INSERT INTO `usuario` (`Usuid`,`usunombre`,`usucontrasena`,`usuultimasesion`,`usuestatus`,`usunombrereal`,`usucorreoe`,`usutelefono`,`usudireccion`) VALUES (1,'admon','MTIzNDU2','2026-01-01','T','Administrador','admon@gmail.com','502-23311017','conocida');

INSERT INTO aplicaciones (Aplcodigo, Aplnombre, Aplestado) VALUES (1, 'LOGIN', '1');