package com.nci.ums.client.ws;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.axiom.om.OMConstants;
import org.apache.axiom.om.OMDataSource;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.llom.OMSourcedElementImpl;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Stub;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.databinding.ADBBean;
import org.apache.axis2.databinding.ADBDataSource;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.databinding.utils.ConverterUtil;
import org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.OutInAxisOperation;
import org.apache.axis2.description.OutOnlyAxisOperation;
import org.apache.axis2.description.WSDL2Constants;

/*
 * UMS3_MsgSendUMS3_MsgSendSOAP12Port_httpStub java implementation
 */

public class UMSSendStub extends Stub {
	protected AxisOperation[] _operations;

	// hashmaps to keep the fault mapping
	private HashMap faultExceptionNameMap = new HashMap();
	private HashMap faultExceptionClassNameMap = new HashMap();
	private HashMap faultMessageMap = new HashMap();

	private void populateAxisService() throws AxisFault {

		// creating the Service with a unique name
		_service = new AxisService("UMS3_MsgSend" + this.hashCode());

		// creating the operations
		AxisOperation __operation;

		_operations = new AxisOperation[2];

		__operation = new OutInAxisOperation();

		__operation.setName(new QName("", "sendWithAck"));
		_service.addOperation(__operation);

		_operations[0] = __operation;

		__operation = new OutOnlyAxisOperation();

		__operation
				.setName(new QName("", "sendWithoutAck"));
		_service.addOperation(__operation);

		_operations[1] = __operation;

	}

	// populates the faults
	private void populateFaults() {

	}

	/**
	 * Constructor that takes in a configContext
	 */
	public UMSSendStub(ConfigurationContext configurationContext,
			String targetEndpoint) throws AxisFault {
		// To populate AxisService
		populateAxisService();
		populateFaults();

		_serviceClient = new ServiceClient(configurationContext, _service);

		configurationContext = _serviceClient.getServiceContext()
				.getConfigurationContext();

		_serviceClient.getOptions().setTo(
				new org.apache.axis2.addressing.EndpointReference(
						targetEndpoint));

		// Set the soap version
		_serviceClient.getOptions().setSoapVersionURI(
				SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);

	}

	/**
	 * Default Constructor
	 */
	public UMSSendStub() throws AxisFault {

		this("http://127.0.0.1:19829/axis2/services/UMS3_MsgSend");

	}

	/**
	 * Constructor taking the target endpoint
	 */
	public UMSSendStub(String targetEndpoint) throws AxisFault {
		this(null, targetEndpoint);
	}

	/**
	 * Auto generated method signature
	 * 
	 * @see com.nci.ums.client.ws.UMS3_MsgSendUMS3_MsgSendSOAP12Port_http#sendWithAck
	 * @param sendWithAck
	 * 
	 */

	public SendWithAckResponse sendWithAck(

	SendWithAck sendWithAck)

	throws RemoteException

	{

		try {
			OperationClient _operationClient = _serviceClient
					.createClient(_operations[0].getName());
			_operationClient.getOptions().setAction("urn:sendWithAck");
			_operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(
					true);

			addPropertyToOperationClient(_operationClient,
					WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

			// create a message context
			MessageContext _messageContext = new MessageContext();

			// create SOAP envelope with that payload
			SOAPEnvelope env = null;

			env = toEnvelope(getFactory(_operationClient.getOptions()
					.getSoapVersionURI()), sendWithAck,
					optimizeContent(new QName("",
							"sendWithAck")));

			// adding SOAP soap_headers
			_serviceClient.addHeadersToEnvelope(env);
			// set the message context with that soap envelope
			_messageContext.setEnvelope(env);

			// add the message contxt to the operation client
			_operationClient.addMessageContext(_messageContext);

			// execute the operation client
			_operationClient.execute(true);

			MessageContext _returnMessageContext = _operationClient
					.getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
			SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

			Object object = fromOM(_returnEnv.getBody().getFirstElement(),
					SendWithAckResponse.class,
					getEnvelopeNamespaces(_returnEnv));
			_messageContext.getTransportOut().getSender().cleanup(
					_messageContext);

			return (SendWithAckResponse) object;

		} catch (AxisFault f) {

			OMElement faultElt = f.getDetail();
			if (faultElt != null) {
				if (faultExceptionNameMap.containsKey(faultElt.getQName())) {
					// make the fault by reflection
					try {
						String exceptionClassName = (String) faultExceptionClassNameMap
								.get(faultElt.getQName());
						Class exceptionClass = Class
								.forName(exceptionClassName);
						Exception ex = (Exception) exceptionClass.newInstance();
						// message class
						String messageClassName = (String) faultMessageMap
								.get(faultElt.getQName());
						Class messageClass = Class.forName(messageClassName);
						Object messageObject = fromOM(faultElt, messageClass,
								null);
						Method m = exceptionClass.getMethod("setFaultMessage",
								new Class[] { messageClass });
						m.invoke(ex, new Object[] { messageObject });

						throw new RemoteException(ex.getMessage(), ex);
					} catch (ClassCastException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (ClassNotFoundException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (NoSuchMethodException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (InvocationTargetException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (IllegalAccessException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (InstantiationException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					}
				} else {
					throw f;
				}
			} else {
				throw f;
			}
		}
	}

	public void sendWithoutAck(SendWithoutAck sendWithoutAck)
			throws RemoteException {

		OperationClient _operationClient = _serviceClient
				.createClient(_operations[1].getName());
		_operationClient.getOptions().setAction("urn:sendWithoutAck");
		_operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

		addPropertyToOperationClient(_operationClient,
				WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

		SOAPEnvelope env = null;
		MessageContext _messageContext = new MessageContext();

		// Style is Doc.

		env = toEnvelope(getFactory(_operationClient.getOptions()
				.getSoapVersionURI()), sendWithoutAck,
				optimizeContent(new QName("",
						"sendWithoutAck")));

		// adding SOAP soap_headers
		_serviceClient.addHeadersToEnvelope(env);
		// create message context with that soap envelope

		_messageContext.setEnvelope(env);

		// add the message contxt to the operation client
		_operationClient.addMessageContext(_messageContext);

		_operationClient.execute(true);

		return;
	}

	/**
	 * A utility method that copies the namepaces from the SOAPEnvelope
	 */
	private Map getEnvelopeNamespaces(SOAPEnvelope env) {
		Map returnMap = new HashMap();
		Iterator namespaceIterator = env.getAllDeclaredNamespaces();
		while (namespaceIterator.hasNext()) {
			OMNamespace ns = (OMNamespace) namespaceIterator.next();
			returnMap.put(ns.getPrefix(), ns.getNamespaceURI());
		}
		return returnMap;
	}

	private QName[] opNameArray = null;

	private boolean optimizeContent(QName opName) {

		if (opNameArray == null) {
			return false;
		}
		for (int i = 0; i < opNameArray.length; i++) {
			if (opName.equals(opNameArray[i])) {
				return true;
			}
		}
		return false;
	}

	// http://127.0.0.1:19829/axis2/services/UMS3_MsgSend

	public static class SendWithoutAck implements
			ADBBean {

		public static final QName MY_QNAME = new QName(
				"http://impl.service.v3.ums.nci.com", "sendWithoutAck", "ns1");

		/**
		 * field for AppID
		 */

		protected String localAppID;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localAppIDTracker = false;

		/**
		 * Auto generated getter method
		 * 
		 * @return String
		 */
		public String getAppID() {
			return localAppID;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            AppID
		 */
		public void setAppID(String param) {

			if (param != null) {
				// update the setting tracker
				localAppIDTracker = true;
			} else {
				localAppIDTracker = true;

			}

			this.localAppID = param;

		}

		/**
		 * field for Password
		 */

		protected String localPassword;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localPasswordTracker = false;

		/**
		 * Auto generated getter method
		 * 
		 * @return String
		 */
		public String getPassword() {
			return localPassword;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Password
		 */
		public void setPassword(String param) {

			if (param != null) {
				// update the setting tracker
				localPasswordTracker = true;
			} else {
				localPasswordTracker = true;

			}

			this.localPassword = param;

		}

		/**
		 * field for BasicMsgsXML
		 */

		protected String localBasicMsgsXML;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localBasicMsgsXMLTracker = false;

		/**
		 * Auto generated getter method
		 * 
		 * @return String
		 */
		public String getBasicMsgsXML() {
			return localBasicMsgsXML;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            BasicMsgsXML
		 */
		public void setBasicMsgsXML(String param) {

			if (param != null) {
				// update the setting tracker
				localBasicMsgsXMLTracker = true;
			} else {
				localBasicMsgsXMLTracker = true;

			}

			this.localBasicMsgsXML = param;

		}

		/**
		 * isReaderMTOMAware
		 * 
		 * @return true if the reader supports MTOM
		 */
		public static boolean isReaderMTOMAware(XMLStreamReader reader) {
			boolean isReaderMTOMAware = false;

			try {
				isReaderMTOMAware = Boolean.TRUE.equals(reader
						.getProperty(OMConstants.IS_DATA_HANDLERS_AWARE));
			} catch (IllegalArgumentException e) {
				isReaderMTOMAware = false;
			}
			return isReaderMTOMAware;
		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return OMElement
		 */
		public OMElement getOMElement(
				final QName parentQName,
				final OMFactory factory) {

			OMDataSource dataSource = new ADBDataSource(
					this, MY_QNAME) {

				public void serialize(XMLStreamWriter xmlWriter)
						throws XMLStreamException {
					SendWithoutAck.this.serialize(MY_QNAME, factory, xmlWriter);
				}

//				//added by wangql
//				public void serialize(MTOMAwareXMLStreamWriter arg0)
//						throws XMLStreamException {
//					
//					
//				}
			};
			return new OMSourcedElementImpl(MY_QNAME, factory, dataSource);

		}

		public void serialize(final QName parentQName,
				final OMFactory factory, XMLStreamWriter xmlWriter)
				throws XMLStreamException {

			String prefix = parentQName.getPrefix();
			String namespace = parentQName.getNamespaceURI();

			if (namespace != null) {
				String writerPrefix = xmlWriter.getPrefix(namespace);
				if (writerPrefix != null) {
					xmlWriter.writeStartElement(namespace, parentQName
							.getLocalPart());
				} else {
					if (prefix == null) {
						prefix = BeanUtil.getUniquePrefix();
					}

					xmlWriter.writeStartElement(prefix, parentQName
							.getLocalPart(), namespace);
					xmlWriter.writeNamespace(prefix, namespace);
					xmlWriter.setPrefix(prefix, namespace);
				}
			} else {
				xmlWriter.writeStartElement(parentQName.getLocalPart());
			}

			if (localAppIDTracker) {
				namespace = "http://impl.service.v3.ums.nci.com";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = BeanUtil.getUniquePrefix();

						xmlWriter.writeStartElement(prefix, "appID", namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "appID");
					}

				} else {
					xmlWriter.writeStartElement("appID");
				}

				if (localAppID == null) {
					// write the nil attribute

					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance", "nil",
							"1", xmlWriter);

				} else {

					xmlWriter.writeCharacters(localAppID);

				}

				xmlWriter.writeEndElement();
			}
			if (localPasswordTracker) {
				namespace = "http://impl.service.v3.ums.nci.com";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = BeanUtil.getUniquePrefix();

						xmlWriter.writeStartElement(prefix, "password",
								namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "password");
					}

				} else {
					xmlWriter.writeStartElement("password");
				}

				if (localPassword == null) {
					// write the nil attribute

					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance", "nil",
							"1", xmlWriter);

				} else {

					xmlWriter.writeCharacters(localPassword);

				}

				xmlWriter.writeEndElement();
			}
			if (localBasicMsgsXMLTracker) {
				namespace = "http://impl.service.v3.ums.nci.com";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = BeanUtil.getUniquePrefix();

						xmlWriter.writeStartElement(prefix, "basicMsgsXML",
								namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "basicMsgsXML");
					}

				} else {
					xmlWriter.writeStartElement("basicMsgsXML");
				}

				if (localBasicMsgsXML == null) {
					// write the nil attribute

					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance", "nil",
							"1", xmlWriter);

				} else {

					xmlWriter.writeCharacters(localBasicMsgsXML);

				}

				xmlWriter.writeEndElement();
			}

			xmlWriter.writeEndElement();

		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(String prefix, String namespace,
				String attName, String attValue, XMLStreamWriter xmlWriter)
				throws XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);

			}

			xmlWriter.writeAttribute(namespace, attName, attValue);

		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public XMLStreamReader getPullParser(QName qName) {

			ArrayList elementList = new ArrayList();
			ArrayList attribList = new ArrayList();

			if (localAppIDTracker) {
				elementList.add(new QName(
						"http://impl.service.v3.ums.nci.com", "appID"));

				elementList.add(localAppID == null ? null : ConverterUtil
						.convertToString(localAppID));
			}
			if (localPasswordTracker) {
				elementList.add(new QName(
						"http://impl.service.v3.ums.nci.com", "password"));

				elementList.add(localPassword == null ? null : ConverterUtil
						.convertToString(localPassword));
			}
			if (localBasicMsgsXMLTracker) {
				elementList.add(new QName(
						"http://impl.service.v3.ums.nci.com", "basicMsgsXML"));

				elementList.add(localBasicMsgsXML == null ? null
						: ConverterUtil.convertToString(localBasicMsgsXML));
			}

			return new ADBXMLStreamReaderImpl(qName, elementList.toArray(),
					attribList.toArray());

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static SendWithoutAck parse(XMLStreamReader reader)
					throws Exception {
				SendWithoutAck object = new SendWithoutAck();
				String nillableValue = null;
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader
							.getAttributeValue(
									"http://www.w3.org/2001/XMLSchema-instance",
									"type") != null) {
						String fullTypeName = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"type");
						if (fullTypeName != null) {
							String nsPrefix = null;
							if (fullTypeName.indexOf(":") > -1) {
								nsPrefix = fullTypeName.substring(0,
										fullTypeName.indexOf(":"));
							}
							nsPrefix = nsPrefix == null ? "" : nsPrefix;

							String type = fullTypeName.substring(fullTypeName
									.indexOf(":") + 1);

							if (!"sendWithoutAck".equals(type)) {
								// find namespace for the prefix
								String nsUri = reader.getNamespaceContext()
										.getNamespaceURI(nsPrefix);
								return (SendWithoutAck) ExtensionMapper
										.getTypeObject(nsUri, type, reader);
							}

						}

					}

					reader.next();

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new QName(
									"http://impl.service.v3.ums.nci.com",
									"appID").equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if (!"true".equals(nillableValue)
								&& !"1".equals(nillableValue)) {

							String content = reader.getElementText();

							object.setAppID(ConverterUtil
									.convertToString(content));

						} else {

							reader.getElementText(); // throw away text nodes
							// if any.
						}

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new QName(
									"http://impl.service.v3.ums.nci.com",
									"password").equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if (!"true".equals(nillableValue)
								&& !"1".equals(nillableValue)) {

							String content = reader.getElementText();

							object.setPassword(ConverterUtil
									.convertToString(content));

						} else {

							reader.getElementText(); // throw away text nodes
							// if any.
						}

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new QName(
									"http://impl.service.v3.ums.nci.com",
									"basicMsgsXML").equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if (!"true".equals(nillableValue)
								&& !"1".equals(nillableValue)) {

							String content = reader.getElementText();

							object.setBasicMsgsXML(ConverterUtil
									.convertToString(content));

						} else {

							reader.getElementText(); // throw away text nodes
							// if any.
						}

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();
					if (reader.isStartElement())
						// A start element we are not expecting indicates a
						// trailing invalid property
						throw new RuntimeException("Unexpected subelement "
								+ reader.getLocalName());

				} catch (XMLStreamException e) {
					throw new Exception(e);
				}

				return object;
			}

		}// end of factory class

	}

	public static class ExtensionMapper {

		public static Object getTypeObject(String namespaceURI,
				String typeName, XMLStreamReader reader) throws Exception {

			throw new RuntimeException("Unsupported type " + namespaceURI + " "
					+ typeName);
		}

	}

	public static class SendWithAck implements
			ADBBean {

		public static final QName MY_QNAME = new QName(
				"http://impl.service.v3.ums.nci.com", "sendWithAck", "ns1");

		/**
		 * field for AppID
		 */

		protected String localAppID;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localAppIDTracker = false;

		/**
		 * Auto generated getter method
		 * 
		 * @return String
		 */
		public String getAppID() {
			return localAppID;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            AppID
		 */
		public void setAppID(String param) {

			if (param != null) {
				// update the setting tracker
				localAppIDTracker = true;
			} else {
				localAppIDTracker = true;

			}

			this.localAppID = param;

		}

		/**
		 * field for Password
		 */

		protected String localPassword;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localPasswordTracker = false;

		/**
		 * Auto generated getter method
		 * 
		 * @return String
		 */
		public String getPassword() {
			return localPassword;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Password
		 */
		public void setPassword(String param) {

			if (param != null) {
				// update the setting tracker
				localPasswordTracker = true;
			} else {
				localPasswordTracker = true;

			}

			this.localPassword = param;

		}

		/**
		 * field for BasicMsgsXML
		 */

		protected String localBasicMsgsXML;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localBasicMsgsXMLTracker = false;

		/**
		 * Auto generated getter method
		 * 
		 * @return String
		 */
		public String getBasicMsgsXML() {
			return localBasicMsgsXML;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            BasicMsgsXML
		 */
		public void setBasicMsgsXML(String param) {

			if (param != null) {
				// update the setting tracker
				localBasicMsgsXMLTracker = true;
			} else {
				localBasicMsgsXMLTracker = true;

			}

			this.localBasicMsgsXML = param;

		}

		/**
		 * isReaderMTOMAware
		 * 
		 * @return true if the reader supports MTOM
		 */
		public static boolean isReaderMTOMAware(XMLStreamReader reader) {
			boolean isReaderMTOMAware = false;

			try {
				isReaderMTOMAware = Boolean.TRUE.equals(reader
						.getProperty(OMConstants.IS_DATA_HANDLERS_AWARE));
			} catch (IllegalArgumentException e) {
				isReaderMTOMAware = false;
			}
			return isReaderMTOMAware;
		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return OMElement
		 */
		public OMElement getOMElement(
				final QName parentQName,
				final OMFactory factory) {

			OMDataSource dataSource = new ADBDataSource(
					this, MY_QNAME) {

				public void serialize(XMLStreamWriter xmlWriter)
						throws XMLStreamException {
					SendWithAck.this.serialize(MY_QNAME, factory, xmlWriter);
				}

				//added by wangql
//				public void serialize(MTOMAwareXMLStreamWriter arg0)
//						throws XMLStreamException {
//					
//					
//				}
			};
			return new OMSourcedElementImpl(MY_QNAME, factory, dataSource);

		}

		public void serialize(final QName parentQName,
				final OMFactory factory, XMLStreamWriter xmlWriter)
				throws XMLStreamException {

			String prefix = parentQName.getPrefix();
			String namespace = parentQName.getNamespaceURI();

			if (namespace != null) {
				String writerPrefix = xmlWriter.getPrefix(namespace);
				if (writerPrefix != null) {
					xmlWriter.writeStartElement(namespace, parentQName
							.getLocalPart());
				} else {
					if (prefix == null) {
						prefix = BeanUtil.getUniquePrefix();
					}

					xmlWriter.writeStartElement(prefix, parentQName
							.getLocalPart(), namespace);
					xmlWriter.writeNamespace(prefix, namespace);
					xmlWriter.setPrefix(prefix, namespace);
				}
			} else {
				xmlWriter.writeStartElement(parentQName.getLocalPart());
			}

			if (localAppIDTracker) {
				namespace = "http://impl.service.v3.ums.nci.com";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = BeanUtil.getUniquePrefix();

						xmlWriter.writeStartElement(prefix, "appID", namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "appID");
					}

				} else {
					xmlWriter.writeStartElement("appID");
				}

				if (localAppID == null) {
					// write the nil attribute

					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance", "nil",
							"1", xmlWriter);

				} else {

					xmlWriter.writeCharacters(localAppID);

				}

				xmlWriter.writeEndElement();
			}
			if (localPasswordTracker) {
				namespace = "http://impl.service.v3.ums.nci.com";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = BeanUtil.getUniquePrefix();

						xmlWriter.writeStartElement(prefix, "password",
								namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "password");
					}

				} else {
					xmlWriter.writeStartElement("password");
				}

				if (localPassword == null) {
					// write the nil attribute

					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance", "nil",
							"1", xmlWriter);

				} else {

					xmlWriter.writeCharacters(localPassword);

				}

				xmlWriter.writeEndElement();
			}
			if (localBasicMsgsXMLTracker) {
				namespace = "http://impl.service.v3.ums.nci.com";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = BeanUtil.getUniquePrefix();

						xmlWriter.writeStartElement(prefix, "basicMsgsXML",
								namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "basicMsgsXML");
					}

				} else {
					xmlWriter.writeStartElement("basicMsgsXML");
				}

				if (localBasicMsgsXML == null) {
					// write the nil attribute

					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance", "nil",
							"1", xmlWriter);

				} else {

					xmlWriter.writeCharacters(localBasicMsgsXML);

				}

				xmlWriter.writeEndElement();
			}

			xmlWriter.writeEndElement();

		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(String prefix, String namespace,
				String attName, String attValue, XMLStreamWriter xmlWriter)
				throws XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);

			}

			xmlWriter.writeAttribute(namespace, attName, attValue);

		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public XMLStreamReader getPullParser(QName qName) {

			ArrayList elementList = new ArrayList();
			ArrayList attribList = new ArrayList();

			if (localAppIDTracker) {
				elementList.add(new QName(
						"http://impl.service.v3.ums.nci.com", "appID"));

				elementList.add(localAppID == null ? null : ConverterUtil
						.convertToString(localAppID));
			}
			if (localPasswordTracker) {
				elementList.add(new QName(
						"http://impl.service.v3.ums.nci.com", "password"));

				elementList.add(localPassword == null ? null : ConverterUtil
						.convertToString(localPassword));
			}
			if (localBasicMsgsXMLTracker) {
				elementList.add(new QName(
						"http://impl.service.v3.ums.nci.com", "basicMsgsXML"));

				elementList.add(localBasicMsgsXML == null ? null
						: ConverterUtil.convertToString(localBasicMsgsXML));
			}

			return new ADBXMLStreamReaderImpl(qName, elementList.toArray(),
					attribList.toArray());

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static SendWithAck parse(XMLStreamReader reader)
					throws Exception {
				SendWithAck object = new SendWithAck();
				String nillableValue = null;
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader
							.getAttributeValue(
									"http://www.w3.org/2001/XMLSchema-instance",
									"type") != null) {
						String fullTypeName = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"type");
						if (fullTypeName != null) {
							String nsPrefix = null;
							if (fullTypeName.indexOf(":") > -1) {
								nsPrefix = fullTypeName.substring(0,
										fullTypeName.indexOf(":"));
							}
							nsPrefix = nsPrefix == null ? "" : nsPrefix;

							String type = fullTypeName.substring(fullTypeName
									.indexOf(":") + 1);

							if (!"sendWithAck".equals(type)) {
								// find namespace for the prefix
								String nsUri = reader.getNamespaceContext()
										.getNamespaceURI(nsPrefix);
								return (SendWithAck) ExtensionMapper
										.getTypeObject(nsUri, type, reader);
							}

						}

					}

					reader.next();

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new QName(
									"http://impl.service.v3.ums.nci.com",
									"appID").equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if (!"true".equals(nillableValue)
								&& !"1".equals(nillableValue)) {

							String content = reader.getElementText();

							object.setAppID(ConverterUtil
									.convertToString(content));

						} else {

							reader.getElementText(); // throw away text nodes
							// if any.
						}

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new QName(
									"http://impl.service.v3.ums.nci.com",
									"password").equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if (!"true".equals(nillableValue)
								&& !"1".equals(nillableValue)) {

							String content = reader.getElementText();

							object.setPassword(ConverterUtil
									.convertToString(content));

						} else {

							reader.getElementText(); // throw away text nodes
							// if any.
						}

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new QName(
									"http://impl.service.v3.ums.nci.com",
									"basicMsgsXML").equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if (!"true".equals(nillableValue)
								&& !"1".equals(nillableValue)) {

							String content = reader.getElementText();

							object.setBasicMsgsXML(ConverterUtil
									.convertToString(content));

						} else {

							reader.getElementText(); // throw away text nodes
							// if any.
						}

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();
					if (reader.isStartElement())
						// A start element we are not expecting indicates a
						// trailing invalid property
						throw new RuntimeException("Unexpected subelement "
								+ reader.getLocalName());

				} catch (XMLStreamException e) {
					throw new Exception(e);
				}

				return object;
			}

		}// end of factory class

	}

	public static class SendWithAckResponse implements
			ADBBean {

		public static final QName MY_QNAME = new QName(
				"http://impl.service.v3.ums.nci.com", "sendWithAckResponse",
				"ns1");

		/**
		 * field for _return
		 */

		protected String local_return;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean local_returnTracker = false;

		/**
		 * Auto generated getter method
		 * 
		 * @return String
		 */
		public String get_return() {
			return local_return;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            _return
		 */
		public void set_return(String param) {

			if (param != null) {
				// update the setting tracker
				local_returnTracker = true;
			} else {
				local_returnTracker = true;

			}

			this.local_return = param;

		}

		/**
		 * isReaderMTOMAware
		 * 
		 * @return true if the reader supports MTOM
		 */
		public static boolean isReaderMTOMAware(XMLStreamReader reader) {
			boolean isReaderMTOMAware = false;

			try {
				isReaderMTOMAware = Boolean.TRUE.equals(reader
						.getProperty(OMConstants.IS_DATA_HANDLERS_AWARE));
			} catch (IllegalArgumentException e) {
				isReaderMTOMAware = false;
			}
			return isReaderMTOMAware;
		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return OMElement
		 */
		public OMElement getOMElement(final QName parentQName,
				final OMFactory factory) {

			OMDataSource dataSource = new ADBDataSource(
					this, MY_QNAME) {

				public void serialize(XMLStreamWriter xmlWriter)
						throws XMLStreamException {
					SendWithAckResponse.this.serialize(MY_QNAME, factory,
							xmlWriter);
				}

				//added by wangql
//				public void serialize(MTOMAwareXMLStreamWriter arg0)
//						throws XMLStreamException {
//					
//					
//				}
			};
			return new OMSourcedElementImpl(MY_QNAME, factory, dataSource);

		}

		public void serialize(final QName parentQName, final OMFactory factory,
				XMLStreamWriter xmlWriter) throws XMLStreamException {

			String prefix = parentQName.getPrefix();
			String namespace = parentQName.getNamespaceURI();

			if (namespace != null) {
				String writerPrefix = xmlWriter.getPrefix(namespace);
				if (writerPrefix != null) {
					xmlWriter.writeStartElement(namespace, parentQName
							.getLocalPart());
				} else {
					if (prefix == null) {
						prefix = BeanUtil.getUniquePrefix();
					}

					xmlWriter.writeStartElement(prefix, parentQName
							.getLocalPart(), namespace);
					xmlWriter.writeNamespace(prefix, namespace);
					xmlWriter.setPrefix(prefix, namespace);
				}
			} else {
				xmlWriter.writeStartElement(parentQName.getLocalPart());
			}

			if (local_returnTracker) {
				namespace = "http://impl.service.v3.ums.nci.com";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = BeanUtil.getUniquePrefix();

						xmlWriter
								.writeStartElement(prefix, "return", namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "return");
					}

				} else {
					xmlWriter.writeStartElement("return");
				}

				if (local_return == null) {
					// write the nil attribute

					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance", "nil",
							"1", xmlWriter);

				} else {

					xmlWriter.writeCharacters(local_return);

				}

				xmlWriter.writeEndElement();
			}

			xmlWriter.writeEndElement();

		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(String prefix, String namespace,
				String attName, String attValue, XMLStreamWriter xmlWriter)
				throws XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);

			}

			xmlWriter.writeAttribute(namespace, attName, attValue);

		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public XMLStreamReader getPullParser(QName qName) {

			ArrayList elementList = new ArrayList();
			ArrayList attribList = new ArrayList();

			if (local_returnTracker) {
				elementList.add(new QName("http://impl.service.v3.ums.nci.com",
						"return"));

				elementList.add(local_return == null ? null : ConverterUtil
						.convertToString(local_return));
			}

			return new ADBXMLStreamReaderImpl(qName, elementList.toArray(),
					attribList.toArray());

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static SendWithAckResponse parse(XMLStreamReader reader)
					throws Exception {
				SendWithAckResponse object = new SendWithAckResponse();
				String nillableValue = null;

				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader
							.getAttributeValue(
									"http://www.w3.org/2001/XMLSchema-instance",
									"type") != null) {
						String fullTypeName = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"type");
						if (fullTypeName != null) {
							String nsPrefix = null;
							if (fullTypeName.indexOf(":") > -1) {
								nsPrefix = fullTypeName.substring(0,
										fullTypeName.indexOf(":"));
							}
							nsPrefix = nsPrefix == null ? "" : nsPrefix;

							String type = fullTypeName.substring(fullTypeName
									.indexOf(":") + 1);

							if (!"sendWithAckResponse".equals(type)) {
								// find namespace for the prefix
								String nsUri = reader.getNamespaceContext()
										.getNamespaceURI(nsPrefix);
								return (SendWithAckResponse) ExtensionMapper
										.getTypeObject(nsUri, type, reader);
							}

						}

					}

					reader.next();

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new QName("http://impl.service.v3.ums.nci.com",
									"return").equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if (!"true".equals(nillableValue)
								&& !"1".equals(nillableValue)) {

							String content = reader.getElementText();

							object.set_return(ConverterUtil
									.convertToString(content));

						} else {

							reader.getElementText(); // throw away text nodes
							// if any.
						}

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();
					if (reader.isStartElement())
						// A start element we are not expecting indicates a
						// trailing invalid property
						throw new RuntimeException("Unexpected subelement "
								+ reader.getLocalName());

				} catch (XMLStreamException e) {
					throw new Exception(e);
				}

				return object;
			}

		}

	}

	private SOAPEnvelope toEnvelope(SOAPFactory factory, SendWithAck param,
			boolean optimizeContent) {
		SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();

		emptyEnvelope.getBody().addChild(
				param.getOMElement(SendWithAck.MY_QNAME, factory));

		return emptyEnvelope;
	}

	/* methods to provide back word compatibility */

	private SOAPEnvelope toEnvelope(SOAPFactory factory, SendWithoutAck param,
			boolean optimizeContent) {
		SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();

		emptyEnvelope.getBody().addChild(
				param.getOMElement(SendWithoutAck.MY_QNAME, factory));

		return emptyEnvelope;
	}

	private Object fromOM(OMElement param, Class type, Map extraNamespaces) {

		try {

			if (SendWithAck.class.equals(type)) {

				return SendWithAck.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (SendWithAckResponse.class.equals(type)) {

				return SendWithAckResponse.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (SendWithoutAck.class.equals(type)) {

				return SendWithoutAck.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}
}
