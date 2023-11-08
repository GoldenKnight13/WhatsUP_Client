# WhatsUP_Client
Proyecto criptografía: Cliente de WhatsUP

## Descripcion
Esta aplicación permite conectarse al servidor de WhatsUP para poder comunicarse con otros usuarios.  <\br>
Se empieza por acceder a una ventana de log-in en donde se pide un usuario y contraseña al usuario. Si esta es válida permite pasar a la sala común, de otra forma indica que las credenciales no son correctas. <\br>
Se accede a la sala común en donde se cargan todas las personas activas al momento, actualizandose cada vez que un nuevo usuario se conecta. Al lado del nombre de los demás usuarios en línea se tiene un botón que permite acceder a un chat directo con esa persona. <\br>
En la ventana del chat se tiene el nombre de la persona con quien se esta chateando, un historial temporal de mensajes y varios botones para enviar distintos tipos de mensaje: 
+ Texto plano
+ Encriptación simétrica
+ Encriptación asimétrica
+ Firma digital
+ Sobre digital
<\br>
En caso de requerir llaves para el cifrado, estas se piden al usuario en una ventana aparte. <\br>
El cifrado utilizado es César. 

## Futuras implementaciones
- Implementación de un request de verificación de usuario al servidor para revisar las credenciales del usuario
- Implementación de request de contactos fijos del usuario, así como un historial de mensajes
- Esta pendiente la implementación una PKI para poder agregar un nuevo nivel de seguridad en la conexión. 


## Por mejorar
* Se puede mejorar el aspecto visual de las pantallas. 
* El color del mensaje se puede cambiar de acuerdo al tipo de mensaje recibido. 
