package fun.vyse.cloud.model.xml;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

@Data
@Slf4j
public class MapperMapHandler extends DefaultHandler {
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
    }
}
