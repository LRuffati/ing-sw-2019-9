ADRENALINA


Gruppo9
Lorenzo Ruffati
Carmelo Sarta
Pietro Tenani


Funzionalità implementate:

- Regole complete

- Modalità dominazione

- Connessione e riconnessione con socket (con possibilità di disconnessione manuale e forzata)
- Connessione e riconnessione con rmi (con possibilità di disconnessione manuale e forzata)

- Interfaccia testuale (CLI) completa
- Interfaccia grafica (GUI) incompleta


Prima di eseguire i jar è necessario seguire alcuni passaggi.
Su Windows occorre abilitare i colori ANSI su terminale eseguendo il comando
REG ADD HKCU\CONSOLE /f /v VirtualTerminalLevel /t REG_DWORD /d 1

È inoltre caldamente consigliato fare in modo di non avere installate interfacce di rete aggiuntive rispetto a quelle standard, in quanto RMI potrebbe connettersi usando una di queste e rendendo quindi inutilizzabile la connessione.

Su Linux occorre modificare il file
etc\hosts
inserendo il proprio indirizzo IP locale. Anche questo si rivela necessario per poter godere di una rete RMI funzionante.


Per eseguire il jar da Windows è sufficiente eseguire il file client.bat o server.bat
Per eseguire il jar da Linux è sufficiente eseguire il file client o server