FROM tomcat:9-alpine
ADD target/*.war /usr/local/tomcat/webapps/
CMD ["catalina.sh", "run"]