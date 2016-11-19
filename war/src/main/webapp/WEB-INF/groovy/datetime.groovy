
log.info "Setting attribute datetime"

request.setAttribute 'datetime', new Date().toString() // <1>

log.info "Forwarding to the template"

forward '/WEB-INF/pages/datetime.gtpl'