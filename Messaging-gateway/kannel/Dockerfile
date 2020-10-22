FROM ubuntu:trusty

RUN apt-get update && apt-get install -y \
    supervisor \
    kannel \
    kannel-extras \
    curl

RUN mkdir /var/spool/kannel

COPY conf/* /etc/kannel/

COPY conf/supervisord.conf /etc/supervisor/conf.d/supervisord.conf

EXPOSE 13013 13000

VOLUME ["/var/spool/kannel", "/etc/kannel", "/var/log/kannel", "/etc/supervisor/conf.d"]

CMD ["/usr/bin/supervisord"]