<!-- Header with logo, project name and group name -->
<p align="center">
  <!-- Project Logo -->
  <img src="" alt="Project Logo" width="150" height="150">
</p>

<!-- Project Title -->
<h1 align="center">LabFlow</h1>

<!-- Group name -->
<p align="center"">
  <i>A Project by The Beauty And The Beast</i>
</p>

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
