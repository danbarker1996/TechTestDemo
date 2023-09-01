package uk.co.gamma.address.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import uk.co.gamma.address.model.Address;
import uk.co.gamma.address.model.Zone;
/**
 * IMPORTANT This service is simulating an API that is slow and throws errors.
 * Please do not change it when performing the coding task.
 *
 */

@Component
public class BlackListService {

    private static final Logger logger = LoggerFactory.getLogger(BlackListService.class);

    private final ObjectMapper objectMapper;

    private final Random random = new Random();
    private List<Zone> zones;


    @Autowired
    BlackListService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Read a list of Zones from the file blackListZones.json
     * IMPORTANT This service is simulating an API that is slow and throws errors.
     * Please do not change it when performing the coding task.
     *
     * @return List of  {@link Zone}. Not @Null
     */
    public List<Zone> getAll() throws IOException, InterruptedException {
        //INTENTIONAL error simulation DO NOT CHANGE.
        final int randomInt = random.nextInt(9);
        if (randomInt  == 1) {
            System.out.println("getall random exception");
            throw new IOException();
        }

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //INTENTIONAL simulation of a slow api call. DO NOT CHANGE.
                    System.out.println("Getting the Zones");
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                zones = loadFile();

            }
        });

        t.start();
        t.join();

        return zones;
    }

    /**
     * loadFile loads the data from the blackListZones.json file.
      
     * @return list of  {@link Zone} objects. Zone contains a postcode string field.
     */
    private List<Zone> loadFile() {
        Resource resource = new ClassPathResource("blackListZones.json");
        try {
            if (resource != null && resource.exists()) {
                TypeReference<List<Zone>> mapType = new TypeReference<List<Zone>>() {
                };
                File file = resource.getFile();
                return objectMapper.readValue(file, mapType);

            } else {
                logger.error("file does not exist");
            }
        } catch (IOException e) {
            logger.error("Exception reading resource " + e);
        }
        return List.of();
    }
}
