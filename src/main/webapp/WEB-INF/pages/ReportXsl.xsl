<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="xml" indent="yes"/>

<xsl:template match="/">
        <kml xmlns="http://earth.google.com/kml/2.1">
            <xsl:apply-templates select="@*|node()"/>           
        </kml>
    </xsl:template>
	
 <xsl:template match="list">
	    <report>
			<xsl:for-each select="report">
		  <Placemark>
				<id><xsl:value-of select="id"/></id>
				<username><xsl:value-of select="username"/></username>
				<title><xsl:value-of select="title"/></title>
				<content><xsl:value-of select="content"/></content>
				<experation><xsl:value-of select="experation"/></experation>
				<Point>
					<coordinates>
						<xsl:value-of select="geolat" />,<xsl:value-of select="geolng" />
					</coordinates>
				</Point>
			  </Placemark>
			</xsl:for-each>
		</report>
	</xsl:template>
</xsl:stylesheet>