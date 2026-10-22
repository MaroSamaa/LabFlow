# LabFlow
Progetto Labflow del gruppo The Beauty and The Beast

## 🚀 Compile and Run
Download the repository ZIP file and extract it or clone the repositry directly in the CLI by typing:
```
git clone https://github.com/MaroSamaa/TestCondiviso
```

### CLI
⚠️ Make sure to be in the project's root directory (check by seeing if the 'src', 'bin' and 'lib' folders are directly in the directory you moved to) before doing any of the following.

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
### IDE
Open the project folder then navigate to
Src->Main.java and run the program.
