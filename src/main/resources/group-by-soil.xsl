<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>
    <xsl:strip-space elements="*"/>

 
    <xsl:key name="plants-by-soil" match="Flower/Plant" use="Soil"/>

    <xsl:template match="/Flower">
        <FlowerGroupedBySoil>
            <xsl:for-each select="Plant[generate-id() = generate-id(key('plants-by-soil', Soil)[1])]">
                <xsl:sort select="Soil"/>
                <SoilGroup soil="{Soil}">
                    <xsl:copy-of select="key('plants-by-soil', Soil)"/>
                </SoilGroup>
            </xsl:for-each>
        </FlowerGroupedBySoil>
    </xsl:template>
</xsl:stylesheet>
