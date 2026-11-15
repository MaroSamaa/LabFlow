<!-- Header with logo, project name and group name -->
<p align="center">
  <!-- Project Logo -->
  <img src="LabFlow/src/images/LabFlowLogo.png" alt="LabFlow Logo" width="150" height="150">
</p>

<!-- Project Title -->
<h1 align="center">LabFlow</h1>

<!-- Group name -->
<p align="center"">
  <i>A Project by The Beauty And The Beast</i>
</p>

## 🎬 Demo

<p align="center">
  <img src="Docs/Demo/DemoVideo.gif" width="100%">
</p>

You can run this demo by loading the Demo save file found in Docs/Demo

## 🚀 Compile and Run
Download the repository ZIP file and extract it or clone the repositry directly in the CLI by typing:
```
git clone https://github.com/MaroSamaa/LabFlow
```

### CLI
⚠️ Make sure to be in the project's root directory (check by seeing if the 'src', 'bin' and 'lib' folders are directly in the directory you moved to) before doing any of the following.

On Windows:
```
cd LabFlow\LabFlow
```
On Linux/MacOS:
```
cd LabFlow/LabFlow
```

#### WINDOWS (CMD)

Compile the source code:
```
dir /s /b src\*.java > sources.txt && javac -cp "lib/*" -d bin @sources.txt && del sources.txt
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

## 📜 Documentation
The documentation is found under Docs.

- Docs/Demo includes the material used in the demo and the demo video;
- Docs/Javadoc includes the javadoc for the program
