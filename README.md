# twitter4jorge
--------------------------------------------------------
Breve explicación de la funcionalidad del servicio:

Las tablas tweet y hashtag se crearan automaticamente en la base de datos al ejecutarse el servicio.

--------------------------------------------------------

start():

-Invocación: /twitter/start/{palabra}

-Se debe introducir una palabra para iniciar la búsqueda, en el caso de no introducirla, el servicio responderá que es necesario.

-Solo buscara tweets en español, frances e italiano.

-Pueden activarse varias busquedas a la vez.

--------------------------------------------------------
stop():

-Invocación: /twitter/stop

-Finaliza todas las busquedas activas.

--------------------------------------------------------
list():

-Invocación: /twitter/list

-Lista todos los tweets guardados, los requisitos son +1500 seguidores.

-Mostrara el id del tweet, usuario, texto, validación.

--------------------------------------------------------

validar():

-Invocación: /twitter/validar/{id}

-Valida un tweet por su ID.

--------------------------------------------------------
validados():

-Invocación: /twitter/validados/

-Muestra una lista con todos los tweets validados

--------------------------------------------------------

hastags():

-Invocación: /twitter/hastags/{limit}

-Muestra una lista de los hastags mas usados de todos los tweets que ha inspeccionado el servicio.

-Si no se indica el limite, por defecto mostrara los 10 mas usados.
