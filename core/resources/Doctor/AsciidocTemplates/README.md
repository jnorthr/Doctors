AsciidocTemplates
=================

TemplateServlet for Asciidoc and Asciidoctor text files

git clone git@github.com:jnorthr/AsciidocTemplates.git

This is a sample groovy template designed to contain asciidoc and asciidoctor template text. It will be used in our Caelyf web services wrapper, hence the package name of groovyx.caelyf.*.

It is is envisioned that the AsciidocTemplate will permit the use of both ${name} and JSP <%= name %> replacement forms. 

The issue at the moment is whether to do something like this:

<% include '/WEB-INF/includes/footer.gtpl' %> and
<% include '/WEB-INF/includes/header.gtpl' %>

as the first step or the last step of the template translation. If done first it could contain both forms of variable replacement and their values would be rendered during the translation. But since the asciidoctorj tool by default converts asciidoc files into html, this would mean that include text snips could NOT contain html streams.

If include's are done as last step, then they could have html streams but no variable replacements. 
