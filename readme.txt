ADRENALINA
ing-sw-2019-9

Gruppo9
Lorenzo Ruffati
Carmelo Sarta
Pietro Tenani



Funzionalità implementate:

- Regole complete

- Modalità dominazione

- Connessione con socket
- Connessione con rmi

- Interfaccia testuale (CLI) completa
- Interfaccia grafica (GUI) incompleta e parzialmente implementata



Per costruire i jar è possibile usare il comando maven "package".

Prima di eseguire i jar è necessario seguire alcuni passaggi.
Windows:
- Occorre abilitare i colori ANSI su terminale eseguendo il comando
REG ADD HKCU\CONSOLE /f /v VirtualTerminalLevel /t REG_DWORD /d 1

- È inoltre caldamente consigliato fare in modo di non avere installate interfacce di rete aggiuntive rispetto a quelle standard, in quanto RMI potrebbe connettersi usando una di queste rendendo quindi inutilizzabile la connessione.

Linux
- Occorre modificare il file
etc\hosts
inserendo il proprio indirizzo IP locale. Anche questo si rivela necessario per poter godere di una rete RMI funzionante.



Per eseguire il jar da Windows è sufficiente eseguire il file client.bat o server.bat
Sia per Windows che per Linux è inoltre possibile eseguire il comando
java -jar seguito dal nome del file (ing-sw-2019-9-client.jar o ing-sw-2019-9-server.jar)
