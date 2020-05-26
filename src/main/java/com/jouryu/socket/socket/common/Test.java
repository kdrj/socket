package com.jouryu.socket.socket.common;

/**
 * @program: socket
 * @description:
 * @author: kdrj
 * @date: 2020-05-18 17:07
 **/
class User {
    private User(){}

    static enum SingletonEnum{
        INSTANCE;
        private User user;

        private SingletonEnum(){
            user=new User();
        }
        public User getInstane(){
            return user;
        }
    }
    public static User getInstance(){
        return SingletonEnum.INSTANCE.getInstane();
    }
}
//public class Test{
//    public static void main(String[] args){
//        User user1=User.getInstance();
//        User user2=User.getInstance();
//        System.out.println(user1==user2);
//        System.out.println(CommondEnumType.REGISTER.getCommond());
//    }
//}
