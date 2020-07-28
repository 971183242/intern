package com.demo.workshop.intern.sso.util;


import com.demo.workshop.intern.sso.exceptions.SSOException;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.Iterator;

/**
 * The SAML helper utils class.
 */
public class SSOSAMLHelper {

    private static final String CLASS_NAME = SSOSAMLHelper.class.getName();

    private Object item = null;

    public static final String SAML_NAME_IDENTIFIER_EXP = "//saml:Assertion/saml:AuthenticationStatement/saml:Subject/saml:NameIdentifier";

    public static final String SAML_AUTHN_METHOD_EXP = "//saml:Assertion/saml:AttributeStatement/saml:Attribute[@AttributeName=\"AUTHN_METHOD\"]/saml:AttributeValue";

    public static final String SAML_ISSUER_EXP = "//saml:Assertion/@Issuer";

    public static final String SAML_ISSUE_INSTANT_EXP = "//saml:Assertion/@IssueInstant";

    public static final String SAML_NOT_BEFORE_EXP = "//saml:Assertion/saml:Conditions/@NotBefore";

    public static final String SAML_NOT_ON_OR_AFTER_EXP = "//saml:Assertion/saml:Conditions/@NotOnOrAfter";

    public static final String WSSE_PREFIX = "wsse";

    public static final String WSSE_NAMESPACE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";

    public static final String SAML_PREFIX = "saml";

    public static final String SAML_NAMESPACE = "urn:oasis:names:tc:SAML:1.0:assertion";

    public static final String ENVELOPE_PREFIX = "env";

    public static final String ENVELOPE_NAMESPACE = "http://schemas.xmlsoap.org/soap/envelope/";

    public static final String DSIG_PREFIX = "ds";

    public static final String DSIG_NAMESPACE = "http://www.w3.org/2000/09/xmldsig#";

    /**
     * Constructor with given item.
     *
     * @param item
     *            the starting context (node or node list, for example).
     * @since SFP.3.3
     */
    public SSOSAMLHelper(Object item) {
        this.item = item;
    }

    /**
     * Constructor with given token string.
     *
     * @param token
     *            the token string.
     * @throws SSOException
     *             if any errors occur.
     * @since SFP.3.3
     */
    public SSOSAMLHelper(String token) throws SSOException {
        setToken(token);
    }

    /**
     * Gets the item.
     *
     * @return the context node or nodelist.
     * @since SFP.3.3
     */
    public Object getItem() {
        return item;
    }

    /**
     * Sets the item.
     *
     * @param item
     *            the context node or nodelist.
     * @since SFP.3.3
     */
    public void setItem(Object item) {
        this.item = item;
    }

    /**
     * Sets the token string.
     *
     * @param token
     *            the token string.
     * @throws SSOException
     *             if any errors occur.
     * @since SFP.3.3
     */
    public void setToken(String token) throws SSOException {
        item = SSOHelperUtils.getNodeFromTokenString(token);
    }

    /**
     * Gets the username from SAML token.
     *
     * @return the username.
     * @since SFP.3.3
     */
    public String getUsername() {
        String username = getNameIdentifier();
        if (username != null && username.length() > 0) {
            int index = username.lastIndexOf("\\");
            if (index > 0) {
                username = username.substring(index + 1);
            }
        }
        return username;
    }

    /**
     * Gets the NameIdentifier from SAML token.
     *
     * @return the NameIdentifier.
     * @since SFP.3.3
     */
    public String getNameIdentifier() {
        return getValue(SAML_NAME_IDENTIFIER_EXP);
    }

    /**
     * Gets the AuthnMethod from SAML token.
     *
     * @return the AuthnMethod.
     * @since SFP.3.3
     */
    public String getAuthnMethod() {
        return getValue(SAML_AUTHN_METHOD_EXP);
    }

    /**
     * Gets the Issuer from SAML token.
     *
     * @return the Issuer.
     * @since SFP.3.3
     */
    public String getIssuer() {
        return getValue(SAML_ISSUER_EXP);
    }

    /**
     * Gets the IssueInstant from SAML token.
     *
     * @return the IssueInstant.
     * @since SFP.3.3
     */
    public String getIssueInstant() {
        return getValue(SAML_ISSUE_INSTANT_EXP);
    }

    /**
     * Gets the NotBefore from SAML token.
     *
     * @return the NotBefore.
     * @since SFP.3.3
     */
    public String getNotBefore() {
        return getValue(SAML_NOT_BEFORE_EXP);
    }

    /**
     * Gets the NotOnOrAfter from SAML token.
     *
     * @return the NotOnOrAfter.
     * @since SFP.3.3
     */
    public String getNotOnOrAfter() {
        return getValue(SAML_NOT_ON_OR_AFTER_EXP);
    }

    /**
     * Gets evaluation of the <code>String</code> value of XPath experssion
     * from SAML token.
     *
     * @param expression
     *            the XPath expression.
     * @return the <code>String</code> that is the result of evaluating the
     *         expression and converting the result to a <code>String</code>.
     * @since SFP.3.3
     */
    public String getValue(String expression) {
        if (item == null) {
            return null;
        }
        final String METHOD_NAME = "getValue(String)";
        XPath xpath = XPathFactory.newInstance().newXPath();
        NamespaceContext ctx = new NamespaceContext() {
            public String getNamespaceURI(String prefix) {
                String uri;
                if (prefix.equals(WSSE_PREFIX))
                    uri = WSSE_NAMESPACE;
                else if (prefix.equals(SAML_PREFIX))
                    uri = SAML_NAMESPACE;
                else if (prefix.equals(ENVELOPE_PREFIX))
                    uri = ENVELOPE_NAMESPACE;
                else if (prefix.equals(DSIG_PREFIX))
                    uri = DSIG_NAMESPACE;
                else
                    uri = null;
                return uri;
            }

            // Dummy implementation - not used!
            public Iterator getPrefixes(String val) {
                return null;
            }

            // Dummy implemenation - not used!
            public String getPrefix(String uri) {
                return null;
            }
        };
        xpath.setNamespaceContext(ctx);
        String value = null;
        try {
            value = xpath.evaluate(expression, item);
        } catch (XPathExpressionException e) {
            // TODO log warning here
//			SSOHelperUtils.getLogger().logWarning(
//					CLASS_NAME + "." + METHOD_NAME,
//					"Cannot evaluate the expression: " + expression, e);
        }
        return value;
    }

}
