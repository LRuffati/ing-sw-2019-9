# Prova finale di Ingegneria del software: AA 2018/19
## Gruppo 9

# Componenti:
+ Lorenzo Ruffati
+ Carmelo Sarta
+ Pietro Tenani

# Homework

## Per 30/04
+ Finire il codice per il Model e parte del Controller
+ Iniziare la parte di Server e la parte di Client (eg. view.cli e/o GUI) che gestiscono la comunicazione (e.g. ricezione e invio di messaggi; chiamate a funzioni in remoto; serializzazione di oggetti; etc.) con Socket e/o RMI
+ Esempio effetti armi con lambdas: un approccio per evitare di avere una miriade di classi con comportamenti hard coded è creare la descrizione degli elementi elementari (eg. gli effetti delle armi) usando JSON/XML e applicare lambdas. Un semplice esempio condiviso anche su piazza si trova al link: https://github.com/ingconti/EffectsUsingLambdas 
+ Test di metodi che lanciano eccezioni: per testare metodi che lanciano eccezioni vi consiglio di seguire questi due link (usate il modo “nativo”, senza Hamcrest o AssertJ):
https://blog.goyello.com/2015/10/01/different-ways-of-testing-exceptions-in-java-and-junit/
https://blog.codeleak.pl/2014/07/junit-testing-exception-with-java-8-and-lambda-expressions.html
Talvolta Sonar e IntelliJ non riescono a calcolare correttamente la coverage nei metodi che lanciano eccezioni, ma non preoccupatevi. 
