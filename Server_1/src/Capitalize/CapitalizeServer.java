package Capitalize;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class CapitalizeServer {

	public static void main(String[] args) throws IOException {
		
		// Server가 준비중이라는 메세지를 콘솔에 출력해준다.
		System.out.println("The capitalization server is running.");
		
		// client의 번호를 매길 수 있는 정수형 변수를 생성
		int clientNumber = 0;
		
		// Date Server때와 동일하게 해당 이클립스에서는 Server를 담당하므로 ServerSocket을 생성한다.
		ServerSocket listener = new ServerSocket(9898);
		
		try {
			/* 
			 * Capitalizer 클래스안에 생성자를 통해서 ServerSocket의 응답과 함께 클라이언트 번호(정수형 변수)를 증가시켜준다.
			   (응답을 받으면 socket에 대한 정보를 담는다.)
			 
			 * start Method를 사용하는 이유는 Thread를 동작시키기 위해 run Method를 실행시켜야 하는데,
			   start Method가 실행되면 run Method를 수행되도록 내부적으로 코딩되어 있기 때문이다.
			    
			 */
			while(true) {				
				new Capitalizer(listener.accept(), clientNumber++).start();
			}
		} finally {
			// 위에서 대문자 처리를 하는 클래스가 모든 일을 수행하고 오면 서버소켓을 닫아준다.
			listener.close();
		}
		 
	}
	
	// private: Class안에서만 참조할 수 있게
	// static: 정적 클래스
	// Thread: 프로세스의 동시처리 혹은 병렬처리를 위한 / 실행하기 위해서는 run()을 사용
	private static class Capitalizer extends Thread {
		
		// Capitalizer Class 안에서 사용할 수 있는 멤버 변수들
		private Socket socket;
		private int clientNumber;
		
		// Constructor
		// Main Class에서 해당 생성자를 생성하여 socket에 대한 정보와 접속한 client 순번을 저장해주는 역할을 한다.
		public Capitalizer(Socket socket, int clientNumber) {
			// this는 위의 멤버 변수를 가리킨다.
			this.socket = socket;
			this.clientNumber = clientNumber;
		}
		
		
		// Thread에 있는 run Method를 오버라이드하여 생성 / Thread를 실행하는 Method
		@Override
		public void run() {
			try {
				// 문자를 읽어 들이는 BufferedReader를 통해서멤버 변수로 있는 socket에 대한 정보를 읽는다.
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				// PrintWriter를 통해서 해당 socket에 출력해준다.
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				
				// 출력해줄 내용들
				out.println("Hello, you are client #" + clientNumber + ".");
				out.println("Enter a line with only a period to quit\n");
				
				// 무한반복문에서 BufferedReader
				while(true) {
					// readLine Method를 통해서 BufferedReader에 들어있는 데이터 한 행을 읽는다.
					String input = in.readLine();
					
					// input이 null이거나 "." 문자만 들어있다면 while문을 나가라
					if(input == null || input.equals(".")) {
						break;
					}
					
					// input의 내용을 대문자로 변경해서 출력하라.
					out.println(input.toUpperCase());
				}				
			} catch (Exception e) {
				// 예외가 발생한다면 log 메소드를 통해 아래와 같이 출력해줘라.
				log("Error handing client # " + clientNumber + ": " + e);
			} finally {
				try {
					// 소켓을 열었으므로 사용이 끝난 후 닫아준다.
					socket.close();
				} catch (Exception e) {
					// 예외가 발생한다면 log를 찍어줘라
					log("Couldn't close a socket, what's going on?");
				}
				// 정상적으로 종료되는 시점에 클라이언트 번호와 함께 출력하라.
				log("Connection with client # " + clientNumber + " closed");
			}
		}
		
		
		private void log(String message) {
			// 들어온 String Class 안에 내용을 그대로 콘솔에 출력해주는 간단 Log Method
			System.out.println(message);
		}
		
	}
}

