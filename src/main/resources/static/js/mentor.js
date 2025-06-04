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
            // "See all messages" 링크를 제외한 모든 항목 제거
            const seeAllLink = messageDropdown.querySelector('.dropdown-item.text-center');
            while (messageDropdown.firstChild) {
                messageDropdown.removeChild(messageDropdown.firstChild);
            }
            if (seeAllLink) {
                messageDropdown.appendChild(seeAllLink);
            }
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
                newMessage.href = `/teaching/mentor/request/${request.mentee_member_id}`;
                newMessage.className = "dropdown-item";
                newMessage.innerHTML = `
                    <div class="d-flex align-items-center">
                        <div class="flex-grow-1">
                            <span class="fw-bold">${request.mentee_nickname || '알 수 없음'}</span>
                            <div class="small text-gray-500">
                                멘토링을 요청했습니다.
                            </div>
                        </div>
                    </div>
                `;

                const divider = document.createElement("hr");
                divider.className = "dropdown-divider";

                // "See all messages" 항목 바로 위에 추가
                const seeAll = messageDropdown.lastElementChild;
                if (seeAll) {
                    messageDropdown.insertBefore(divider, seeAll);
                    messageDropdown.insertBefore(newMessage, divider);
                }
            });
        } catch (error) {
            console.error("Error updating messages:", error);
        }
    }

    function checkMentorRequests() {
        console.log("Checking mentor requests...");
        fetch('/teaching/mentor/requested-applies')
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