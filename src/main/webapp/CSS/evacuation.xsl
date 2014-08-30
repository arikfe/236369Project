<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="xml" indent="yes"/>

<xsl:template match="/">
        <kml xmlns="http://earth.google.com/kml/2.1">
            <xsl:apply-templates select="@*|node()"/>           
        </kml>
    </xsl:template>
	
 <xsl:template match="list">
	    <Evacuation>
			<xsl:for-each select="com.technion.project.model.EvacuationEvent">
		  <Placemark>
				<id><xsl:value-of select="id"/></id>
				<estimated ><xsl:value-of select="estimated "/></estimated >
				<means><xsl:value-of select="means"/></means>
				<capacity><xsl:value-of select="capacity"/></capacity>
				<Point>
					<coordinates>
						<xsl:value-of select="geolat" />,<xsl:value-of select="geolng" />
					</coordinates>
				</Point>
			  </Placemark>
			  <users>
				<xsl:for-each select="registeredUsers/storedSnapshot/entry">
					<username><xsl:value-of select="com.technion.project.model.User/username"/></username>
				</xsl:for-each>
			  </users>
			  
			</xsl:for-each>
		</Evacuation>
	</xsl:template>
</xsl:stylesheet>