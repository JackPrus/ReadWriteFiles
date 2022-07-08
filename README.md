# ReadWriteFiles
Application rad all .txt files in directury (including inner directories) and sort by files name and write all read information to one newly created file


For starting of program you have to open console in folder the .java file placed
And write down following comands (create .bat file including following): 

javac FileReading.java

TIMEOUT /T 2 /NOBREAK 

REM change CHCP to UTF-8
CHCP 65001
CLS

TIMEOUT /T 2 /NOBREAK

java -classpath . FileReading
