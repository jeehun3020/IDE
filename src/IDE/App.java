package IDE;

import java.util.Scanner;

public class App {
    private UploadFile uploadFile;
    private Controller controller;

    /**
     * IDE 메인 실행 메소드
     */
    public void run() {
        try {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                Interface.showMainScreen(this.uploadFile);

                int choice = scanner.nextInt();
                switch (choice) {
                    case 1 -> uploadFiles(scanner);
                    case 2 -> compileFiles();
                    case 3 -> runFiles();
                    case 4 -> reset();
                    case 5 -> showCompileError();
                    case 6 -> exit();
                    default -> System.out.println("잘못 선택하셨습니다.\n다시 선택해 주세요.");
                }
            }
        } catch (Exception e) {
            System.out.printf("프로그램 실행에 문제가 발생했습니다.\nError: %s", e.getMessage());
        }
    }

    /**
     * 파일을 업로드하는 메소드입니다.
     *
     * @param scanner 입력 Scanner
     * **/
    private void uploadFiles(Scanner scanner) {
        if (this.uploadFile != null) {
            System.out.println("이미 업로드 된 파일이 존재합니다.\n파일을 삭제하고 싶으시다면 Reset을 진행해 주세요.");
            return;
        }

        try {
            Interface.showUploadScreen();

            scanner.nextLine();    // Scanner 버퍼 초기화
            String filePath = scanner.nextLine();

            this.uploadFile = new UploadFile(filePath);
            System.out.println("업로드가 완료되었습니다.");
        } catch (Exception e) {
            System.out.printf("업로드에 문제가 발생했습니다.\nError: %s\n", e);
        }
    }

    /**
     * 업로드 된 파일을 컴파일합니다.
     * <p>
     * - 업로드 확인
     * - ProcessBuilder 명령어 실행
     *       1. main 함수 파일 컴파일         javac main.java
     *       2. 패키지단 디렉토리 파일 컴파일    javac *.java
     * - 컴파일 결과 저장
     *       : 컴파일 여부
     *       : 오류 발생 여부
     *       : 외부 터미널 출력 결과
     */
    private void compileFiles() {
        try {
            if (this.uploadFile == null) {
                System.out.println("업로드 된 파일이 없습니다.\n먼저 업로드를 진행해 주세요.");
                return;
            }

            this.controller = new Controller();
            this.controller.compile(uploadFile);
        } catch (Exception e) {
            System.out.printf("컴파일 중 문제가 발생했습니다.\nError: %s\n", e);
        }
    }

    /**
     * 컴파일 된 파일을 실행합니다.
     * <p>
     * - 업로드, 컴파일 상태 확인
     * - ProcessBuilder 명령어 실행
     */
    private void runFiles() {
        try {
            // ! 업로드 상태, 컴파일 상태 둘 다 확인 필요
            if (this.uploadFile == null) {
                System.out.println("업로드된 파일이 없습니다.\n먼저 파일을 업로드하고 컴파일하세요.");
                return;
            }

            this.controller.run(uploadFile);
        } catch (Exception e) {
            System.out.printf("실행 중 문제가 발생했습니다.\nError: %s\n", e);
        }
    }

    /**
     * 모든 상태를 초기화합니다.
     */
    private void reset() {
        try {
            this.uploadFile = null;
            this.controller = null;
            System.out.println("IDE의 모든 상태를 초기화했습니다.");
        } catch (Exception e) {
            System.out.printf("리셋 진행 중 문제가 발생했습니다.\nError: %s\n", e);
        }
    }

    /**
     * 컴파일 시 발생한 오류를 출력합니다.
     */
    private void showCompileError() {
        try {
            if (this.controller == null) {
                System.out.println("컴파일 기록이 존재하지 않습니다.");
            } else {
                this.controller.showError();
            }
        } catch (Exception e) {
            System.out.printf("컴파일 오류 메시지 출력에 문제가 발생했습니다.\nError: %s\n", e);
        }
    }

    /**
     * 프로그램을 종료합니다.
     */
    private void exit() {
        System.out.print("프로그램을 종료합니다.");
        System.exit(0);
    }
}