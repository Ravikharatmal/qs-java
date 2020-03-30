<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../partials/head.jsp"/>

<div id="index-page">
  <h2>Welcome</h2>
  <p>This launcher demonstrates three quickstart code examples using the DocuSign Java SDK.</p>
  <p>These examples don't include authentication. Instead, use the
    <a href="https://developers.docusign.com/oauth-token-generator" target="_blank">OAuth Token Generator</a>
  to obtain a token.</p>
  <p><b>Configure</b> the examples by editing their source. See the three QS* controllers in the
    controller directory.</p>

  <h4 id="example001">1. <a href="qs01">Embedded Signing Ceremony</a></h4>
  <p>This quickstart example sends an envelope, and then uses an embedded signing ceremony for the signer.
    With embedded signing, the DocuSign signing ceremony is initiated from your website.
  </p>

  <h4 id="example002">2. <a href="qs02">Send an envelope with a remote (email) signer</a></h4>
  <p>The envelope sends a pdf document for signing. The signer receives an email invitation to the signing ceremony.
  </p>

  <h4 id="example003">3. <a href="qs03">List updated envelopes in the user's account</a></h4>
  <p>List the envelopes created or updated in the last 10 days.
  </p>
  
   <h4 id="example003">4. <a href="tu">Send envelop using template</a></h4>
  <p>Before running this, Go to https://appdemo.docusign.com/templates & create template by uploading PDF form.
  </p>
  
     <h4 id="example003">4. <a href="getdoc">Download signed document/envelop</a></h4>
  <p>
  </p>

    <div class="alert alert-info" role="alert" style="display:inline-block;">
    For a full set of Java code examples, including OAuth authentication, see the
    <a href="https://github.com/docusign/eg-03-java-auth-code-grant" target="_blank">Java Code Example Launcher.</a>
  </div>


</div>

<!-- anchor-js is only for the index page -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/anchor-js/4.1.1/anchor.min.js"></script>
<script>anchors.options.placement = 'left'; anchors.add('h4')</script>

<jsp:include page="../partials/foot.jsp"/>
