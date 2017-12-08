package Date;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class DateServer {

	public static void main(String[] args) throws IOException {
		
		// ServerSocket에 listener라는 이름과 함께 서버 소켓(port: 9090)을 생성
		ServerSocket listener = new ServerSocket(9090);
		
		try {
			// while문을 통해서 리스너(서버소켓)를 계속 응답받을 수 있게 true로 계속 실행하게 만든다.
			while(true) {
				// 서버 소켓으로부터 연결을 받아들이면 Client측으로부터 입력받은 ip와 port가 Socket에 저장된다. 
				Socket socket = listener.accept();
				//System.out.println(socket.toString());
				
				try {
					// PrintWriter를 통해서 socket에 저장된 Client에게 OutputStream으로 출력해준다.
					/* 
					 * Flush: 출력버퍼에 임시로 보관되어 스트림으로 출력될 때까지 대기중인 데이터를 스트림으로 내보내는 것
					 * 
					 * autoFlush: 해당 옵션이 true일 경우 print() 또는 write() Method의 경우엔 상관없지만, println() 메소드가 호출되면 자동으로 flush() 메소드를 호출
					*/
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					
					//출력할 내용은 Date Class를 통하여 오늘 날짜를 출력해준다.
					out.println(new Date().toString());
				} finally {
					// finally 안에서는 예외 발생 유무, catch(예외) 유무와 상관없이 무조건 수행되는 부분이다.
					// socket을 열어줬으면 닫아줘야하기 때문에 닫아준다.
					socket.close();
				}
			}			
		} finally {
			// ServerSocket의 작업이 종료되었으면 ServerSocket도 닫아줘야한다.
			listener.close();
		}				
	}
}
