#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service;

import org.springframework.stereotype.Service;

@Service
public class SampleServiceImpl implements SampleService {

    public String sampleMethod(String input){
        return input;
    }
}