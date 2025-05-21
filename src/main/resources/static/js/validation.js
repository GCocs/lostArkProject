/**
 * 유효성 검증 함수
 * @param {'nickname' | 'password' | 'email'} type - 검증 타입 지정
 * @param {string} value - 검증할 데이터
 */
export function valid(type, value) {
	const nicknameRegex = /^[가-힣a-zA-Z0-9]+$/;
	const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[0-9])/;
	const emailRegex =  /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;

	if(type === 'nickname') {
		if(value.length <= 0) {
			alert('닉네임을 입력해주세요.');
			return false;
		}
		if(value.length > 12) {
			alert('닉네임을 12자 이내로 입력해주세요.');
			return false;
		}
		if(!nicknameRegex.test(value)) {
			alert('닉네임에 특수문자를 입력할 수 없습니다.');
			return false;
		}

		return true;
	}

	if(type === 'password') {
        if (value == '') {
            alert('비밀번호를 입력해주세요.');
            return false;
        }
		if (!passwordRegex.test(value)) {
            alert('비밀번호는 영문+숫자로 구성하여야 합니다.');
            return false;
        }
	}
	
	if(type === 'email') {
		if(value == '') {
            alert('이메일을 입력해주세요.');
            return false;
        }
		if (!emailRegex.test(value)) {
            alert('이메일 형식으로 입력해주세요.');
            return false;
        }
	}
}