# Library Management System 

Team members:
* Finn O'Neill [@o-Fon-o](https://github.com/o-Fon-o)
* Ethan Cairney  [@ethanCairney98](https://github.com/ethanCairney98)
* Ronan Mascarenhas [@RonanMascarenhas](https://github.com/RonanMascarenhas)

How to Run:
- Running in both docker and maven

in application properties the h2-dialect  
maven username: =sa  
maven password: =password

to build it in docker we used the commands  
docker build -t project-1:latest .

and to run it  
docker run --name project-1 -p 8080:8080 project-1:latest


