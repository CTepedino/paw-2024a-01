

## Credenciales de usuarios ya existentes:

    - Rol "READER": 
        email: testReader@mail.com
        contraseña: 123456

    - Rol "WRITER": 
        email: testWriter@mail.com
        contraseña: 123456

## Necesario antes de ejecutar de forma local:
    
Se debe crear en webapp/src/main/resources un archivo application.properties especificando

    jdbc.url
    jdbc.username
    jdbc.password
    
    mail.host
    mail.username
    mail.password
    
    baseUrl