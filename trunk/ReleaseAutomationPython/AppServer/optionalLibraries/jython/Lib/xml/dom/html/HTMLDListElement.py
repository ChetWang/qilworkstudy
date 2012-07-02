########################################################################
#
# File Name:            HTMLDListElement
#
# Documentation:        http://docs.4suite.com/4DOM/HTMLDListElement.html
#

### This file is automatically generated by GenerateHtml.py.
### DO NOT EDIT!

"""
WWW: http://4suite.com/4DOM         e-mail: support@4suite.com

Copyright (c) 2000 Fourthought Inc, USA.   All Rights Reserved.
See  http://4suite.com/COPYRIGHT  for license and copyright information
"""

import string
from xml.dom import Node
from xml.dom.html.HTMLElement import HTMLElement

class HTMLDListElement(HTMLElement):

    def __init__(self, ownerDocument, nodeName="DL"):
        HTMLElement.__init__(self, ownerDocument, nodeName)

    ### Attribute Methods ###

    def _get_compact(self):
        return self.hasAttribute("COMPACT")

    def _set_compact(self, value):
        if value:
            self.setAttribute("COMPACT", "COMPACT")
        else:
            self.removeAttribute("COMPACT")

    ### Attribute Access Mappings ###

    _readComputedAttrs = HTMLElement._readComputedAttrs.copy()
    _readComputedAttrs.update({
        "compact" : _get_compact
        })

    _writeComputedAttrs = HTMLElement._writeComputedAttrs.copy()
    _writeComputedAttrs.update({
        "compact" : _set_compact
        })

    _readOnlyAttrs = filter(lambda k,m=_writeComputedAttrs: not m.has_key(k),
                     HTMLElement._readOnlyAttrs + _readComputedAttrs.keys())
