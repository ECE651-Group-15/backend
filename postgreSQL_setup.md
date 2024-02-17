- Use `sudo apt update` to update the package list.

- Use `sudo apt install postgresql postgresql-contrib` to install PostgreSQL.

- Use `sudo -i -u postgres` to switch to the postgres user.

- Use `psql` to open the PostgreSQL prompt.

- Use `CREATE DATABASE mydatabase;` to create a new database.

- Use `\l` to list all databases.

- Use `\c mydatabase` to connect to the new database.

- use `\q` to quit the PostgreSQL prompt.

- Use `exit` to exit the postgres user.

- Use `sudo -u postgres createuser --interactive` to create a new user.

- Use `\password postgres` to set a password for the `postgres` user.

export MAVEN_HOME=/opt/apache-maven-3.9.6
export PATH=$PATH:$MAVEN_HOME/bin

export JAVA_HOME=/opt/amazon-corretto-17.0.10.8.1-linux-x64
export PATH=$PATH:$JAVA_HOME/bin

nohup java -jar target/quarkus-app/quarkus-run.jar > app.log 2>&1 &
