```sql
create user 'jwt'@'%' identified by 'jwt1234';
GRANT ALL PRIVILEGES ON *.* TO 'jwt'@'%';
create database jwtsecurity;
use jwtsecurity;
```
```java
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
//자바 시크릿키 생성
public class RandomKeyGeneration {
    public static void main(String[] args) {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String secretString = Encoders.BASE64.encode(key.getEncoded());
        System.out.println("Secret key: " + secretString);
    }
}
```