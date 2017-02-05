package com.jos.dem.vetlog.service.impl

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value

import com.jos.dem.vetlog.service.RecoveryService
import com.jos.dem.vetlog.service.RestService
import com.jos.dem.vetlog.model.RegistrationCode
import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.command.MessageCommand
import com.jos.dem.vetlog.repository.RegistrationCodeRepository

@Service
class RecoveryServiceImpl implements RecoveryService {

  @Autowired
  RestService restService
  @Autowired
  RegistrationCodeRepository repository

  @Value('${serverName}')
  String serverName
  @Value('${template.register}')
  String template


  void sendConfirmationAccountToken(String email){
    RegistrationCode registrationCode = new RegistrationCode(email:email)
    repository.save(registrationCode)
    Command command = new MessageCommand(email:email, template:template, url:"${serverName}${registrationCode.token}")
    restService.sendCommand(command)
  }

  User confirmAccountForToken(String token){
    User user = getUserByToken(token)
    user.enabled = true
    userRepository.save(user)
    user
  }

  User getUserByToken(String token){
    String email = registrationService.findEmailByToken(token)
    User user = userRepository.findByEmail(email)
    user
  }

}
