server {
    listen       80; #HTTP 的端口
    server_name  ${domain};

    #动态代理
    location / {
        proxy_pass  http://${tomcat_ip}:${tomcat_port}; #反向代理

    index  index.jsp index.html index.htm;
        access_log off;
        proxy_redirect    off;
        proxy_set_header   Host $host;
        proxy_set_header   X-Real-IP $remote_addr;
        proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
        client_max_body_size   10m;
        client_body_buffer_size   128k;
        proxy_connect_timeout   300;
        proxy_send_timeout   300;
        proxy_read_timeout   300;
        proxy_buffer_size   4k;
        proxy_buffers   4 32k;
        proxy_busy_buffers_size   64k;
        proxy_temp_file_write_size  64k;
    }
}