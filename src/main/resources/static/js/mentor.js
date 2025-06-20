// 멘토/멘티 관련 JavaScript 코드
document.addEventListener('DOMContentLoaded', function() {
    const messageDropdown = document.getElementById("messageDropdownMenu");
    const messageBadge = document.getElementById('messageBadge');

    if (!messageDropdown) {
        console.log("Message dropdown not found");
        return;
    }

    const shownRequests = new Set(); // 중복 방지용 캐시

    // 기존 메시지 초기화 함수
    function clearMessages() {
        try {
            // 모든 자식 제거
            while (messageDropdown.firstChild) {
                messageDropdown.removeChild(messageDropdown.firstChild);
            }
            // 동적 메시지와 고정 메뉴 사이 구분선
            const fixedDivider = document.createElement('hr');
            fixedDivider.className = 'dropdown-divider fixed-divider';
            // 고정 메뉴 두 개를 새로 생성해서 추가
            const link1 = document.createElement('a');
            link1.href = '/message/list';
            link1.className = 'dropdown-item text-center';
            link1.textContent = '받은 멘토링 신청 전부보기';

            const link2 = document.createElement('a');
            link2.href = '/message/myRequest';
            link2.className = 'dropdown-item text-center';
            link2.textContent = '내가 보낸 멘토링 신청 결과';

            // 구분선 + 고정 메뉴 추가
            messageDropdown.appendChild(fixedDivider);
            messageDropdown.appendChild(link1);
            messageDropdown.appendChild(link2);
        } catch (error) {
            console.error("Error clearing messages:", error);
        }
    }

    function updateMessages(data) {
        try {
            clearMessages(); // 기존 메시지 초기화
            shownRequests.clear(); // 상태 캐시 초기화

            // 알림 배지 업데이트
            if (messageBadge) {
                if (data && data.length > 0) {
                    messageBadge.textContent = data.length;
                    messageBadge.style.display = 'inline-block';
                    console.log("Updating badge with count:", data.length);
                } else {
                    messageBadge.style.display = 'none';
                    console.log("Hiding badge - no messages");
                }
            } else {
                console.log("Message badge element not found");
            }

            if (!data || data.length === 0) {
                console.log("No requests found");
                return;
            }

            console.log("Processing requests:", data);

            // 고정 메뉴(구분선) 앞에 동적 메시지 추가
            const fixedDivider = messageDropdown.querySelector('.fixed-divider');

            data.forEach(request => {
                if (!request || !request.mentee_member_id) {
                    console.log("Invalid request data:", request);
                    return;
                }

                const key = request.mentee_member_id;
                if (shownRequests.has(key)) {
                    console.log("Skipping duplicate request:", key);
                    return;
                }
                shownRequests.add(key);

                // 새 메시지 항목 생성
                const newMessage = document.createElement("a");
                newMessage.href = `/message/newMessageDetail?menteeMemberId=${request.mentee_member_id}`;
                newMessage.className = "dropdown-item";

                let statusMessage = '';
                switch (request.apply_status) {
                    case 'ACCEPTED':
                        statusMessage = '멘토링이 수락되었습니다.';
                        break;
                    case 'REJECTED':
                        statusMessage = '멘토링이 거절당했습니다.';
                        break;
                    default:
                        statusMessage = '멘토링 요청이 도착했습니다.';
                }

                newMessage.innerHTML = `
                    <div class="d-flex align-items-center">
                        <div class="flex-grow-1">
                            <span class="fw-bold">${request.mentee_nickname || '알 수 없음'}</span>
                            <div class="small text-gray-500">
                                ${statusMessage}
                            </div>
                        </div>
                    </div>
                `;

                // 고정 구분선(fixed-divider) 앞에 동적 메시지 삽입
                if (fixedDivider) {
                    messageDropdown.insertBefore(newMessage, fixedDivider);
                } else {
                    messageDropdown.appendChild(newMessage);
                }
            });
        } catch (error) {
            console.error("Error updating messages:", error);
        }
    }

    function checkMentorRequests() {
        console.log("Checking mentor requests...");
        fetch('/message/all-applies')
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                console.log("Received data:", data);
                updateMessages(data);
            })
            .catch(error => {
                console.error('메시지 상태 확인 중 오류 발생:', error);
            });
    }

    // 초기 로딩 시 메시지 표시
    checkMentorRequests();

    // 30초마다 상태 확인
    setInterval(checkMentorRequests, 30000);
}); 