// 멘토 관련 JavaScript 코드
document.addEventListener('DOMContentLoaded', function() {
    const menteeMemberId = document.getElementById('menteeMemberId')?.value;
    if (!menteeMemberId) return;

    const messageDropdown = document.getElementById("messageDropdownMenu");
    if (!messageDropdown) return; // 메시지 드롭다운이 없는 경우 함수 종료

    const shownStatuses = new Set(); // 중복 방지용 캐시

    // 기존 메시지 초기화 함수
    function clearMessages() {
        // "See all messages" 링크를 제외한 모든 항목 제거
        while (messageDropdown.firstChild) {
            if (messageDropdown.firstChild.classList.contains('dropdown-item') && 
                messageDropdown.firstChild.textContent === 'See all messages') {
                break;
            }
            messageDropdown.removeChild(messageDropdown.firstChild);
        }
    }

    function updateMessages(data) {
        clearMessages(); // 기존 메시지 초기화
        shownStatuses.clear(); // 상태 캐시 초기화

        data.forEach(status => {
            const key = status.mentor_member_id + '-' + status.apply_status;
            if (shownStatuses.has(key)) return;
            shownStatuses.add(key);

            let messageText = "";
            let linkUrl = "/teaching/message"; // 알림 클릭 시 이동할 URL

            if (status.apply_status === 'ACCEPTED') {
                messageText = `멘토가 수락했습니다! 디스코드: ${status.discord_id}`;
            } else if (status.apply_status === 'REJECTED') {
                messageText = `멘토가 신청을 거절했습니다.`;
            } else if (status.apply_status === 'REQUESTED') {
                messageText = `멘토에게 신청했습니다.`;
            } else {
                return; // 처리할 상태가 아니면 무시
            }

            // 새 메시지 항목 생성
            const newMessage = document.createElement("a");
            newMessage.href = linkUrl;
            newMessage.className = "dropdown-item";
            newMessage.textContent = messageText;

            const divider = document.createElement("hr");
            divider.className = "dropdown-divider";

            // "See all messages" 항목 바로 위에 추가
            const seeAll = messageDropdown.lastElementChild;
            if (seeAll) {
                messageDropdown.insertBefore(divider, seeAll);
                messageDropdown.insertBefore(newMessage, divider);
            }
        });
    }

    function pollApplyStatus() {
        fetch('/teaching/mentee/apply-status-detail/' + menteeMemberId)
            .then(res => res.json())
            .then(data => {
                updateMessages(data);
            })
            .catch(error => {
                console.error('메시지 상태 확인 중 오류 발생:', error);
            });
    }

    // 초기 로딩 시 메시지 표시
    pollApplyStatus();

    // 10초마다 상태 확인
    setInterval(pollApplyStatus, 10000);
}); 