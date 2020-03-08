# Project 1
Library Management System 

Finn O'Neill - 17367986
Ethan Cairney - 17320391
Ronan Mascarenhas - 17379773

Running in both docker and maven

in application properties the h2-dialect
maven username: =sa
maven password: =password

to build it in docker we used the commands
docker build -t project-1:latest .

and to run it 
docker run --name project-1 -p 8080:8080 project-1:latest