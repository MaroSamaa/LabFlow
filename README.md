# LabFlow
Progetto Labflow del gruppo The Beauty and The Beast

## 🚀 Compile and Run
Download the repository ZIP file and extract it or clone the repositry directly in the CLI by typing:
```
git clone https://github.com/MaroSamaa/TestCondiviso
```

### CLI
#### WINDOWS

Compile the source code:
```
javac -cp "lib/*" -d bin src/**/*.java
```
Run:
```
java -cp "bin;lib/*" Main
```
#### Linux/MacOS
Compile the source code:
```
javac -cp "lib/*" -d bin $(find src -name "*.java")
```
Run:
```
java -cp "bin:lib/*" Main
```
