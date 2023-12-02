FROM tomcat
ADD target/*.war /usr/local/tomcat/webapps/
CMD ["catalina.sh", "run"]
EXPOSE 8080