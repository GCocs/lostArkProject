import { valid } from './validation.js';

let imageApiTimer = null;
let resetTimer = null;

$(() => {
    // 인증 상태 초기화 (세션 삭제)
    resetCertificationState();

    // 이벤트 바인딩
    $('#certification-button').click(fetchCharacterImage);
    $('#reset-certification-button').click(resetCertificationState);
    $('#nickname').keydown((e) => {
        if(e.key === 'Enter') {
            fetchCharacterImage();
        }
    });
});

// 초기 이미지를 페이지에 렌더링
function renderDefaultImage() {
    $('.image-container').empty();

    const imgDom = `
        <img class="w-100 mb-4" src="/img/default_loacon${Math.ceil(Math.random() * 3)}.png" style="max-width:550px; max-height:550px;" />
        <p>닉네임 입력 후에 인증 요청해주세요.</p>
    `;
    $('.image-container').html(imgDom);
}

// 캐릭터 이미지 불러오기
function fetchCharacterImage() {
    // 1. 과도한 api 요청 방지를 위한 스로틀링
    if(imageApiTimer) {
        alert('잠시 후 다시 시도해주세요.');
        return;
    }
    
    imageApiTimer = setTimeout(() => {
        console.log('인증요청 가능');
        imageApiTimer = null;
    }, 5000);
    
    // 2. 캐릭터 닉네임 검증
    const $nickname = $('#nickname');
    if(!valid('nickname', $nickname.val())) {
        renderCharacterImage(null);
        $nickname.focus();
        return;
    }
    
    // 3. api 요청
    $.ajax({
        url: `/member/${$nickname.val()}/certification`,
        method: 'GET',
        success: function(data) {
            renderCharacterImage(data);
        },
        error: (xhr) => {
            console.error("error: ", xhr);
        }
    })
}

// 캐릭터 이미지를 페이지에 렌더링
function renderCharacterImage(data) {
    console.log(data);
    
    $('.image-container').empty();
    
    if(data) {
        const characterImage = data.characterImage.CharacterImage;
        const equipment = data.equipment;

        // 캐릭터 이미지 표시
        const imgDom = `
            <div class="position-relative image-wrapper">
                <img class="position-absolute" src="${equipment['무기'].icon}" style="width:36px; left:calc(50% - 30%); transform:translate(-50%, -50%); top:calc(1 / 8 * 100%); border-radius:3px; opacity:${equipment['무기'].unequippedRequired == true ? '0.3' : '1' }; background-color:${selectBackground(equipment['무기'].grade)}" />
                <img class="position-absolute" src="${equipment['투구'].icon}" style="width:36px; left:calc(50% - 30%); transform:translate(-50%, -50%); top:calc(2 / 8 * 100%); border-radius:3px; opacity:${equipment['투구'].unequippedRequired == true ? '0.3' : '1' }; background-color:${selectBackground(equipment['투구'].grade)}" />
                <img class="position-absolute" src="${equipment['상의'].icon}" style="width:36px; left:calc(50% - 30%); transform:translate(-50%, -50%); top:calc(3 / 8 * 100%); border-radius:3px; opacity:${equipment['상의'].unequippedRequired == true ? '0.3' : '1' }; background-color:${selectBackground(equipment['상의'].grade)}" />
                <img class="position-absolute" src="${equipment['하의'].icon}" style="width:36px; left:calc(50% - 30%); transform:translate(-50%, -50%); top:calc(4 / 8 * 100%); border-radius:3px; opacity:${equipment['하의'].unequippedRequired == true ? '0.3' : '1' }; background-color:${selectBackground(equipment['하의'].grade)}" />
                <img class="position-absolute" src="${equipment['장갑'].icon}" style="width:36px; left:calc(50% - 30%); transform:translate(-50%, -50%); top:calc(5 / 8 * 100%); border-radius:3px; opacity:${equipment['장갑'].unequippedRequired == true ? '0.3' : '1' }; background-color:${selectBackground(equipment['장갑'].grade)}" />
                <img class="position-absolute" src="${equipment['어깨'].icon}" style="width:36px; left:calc(50% - 30%); transform:translate(-50%, -50%); top:calc(6 / 8 * 100%); border-radius:3px; opacity:${equipment['어깨'].unequippedRequired == true ? '0.3' : '1' }; background-color:${selectBackground(equipment['어깨'].grade)}" />
                <img class="position-absolute" src="${equipment['나침반'].icon}" style="width:36px; left:calc(50% - 30%); transform:translate(-50%, -50%); top:calc(7 / 8 * 100%); border-radius:3px; opacity:${equipment['나침반'].unequippedRequired == true ? '0.3' : '1' }; background-color:${selectBackground(equipment['나침반'].grade)}" />
                
                <img class="w-100" src="${characterImage}" />
                
                <img class="position-absolute" src="${equipment['목걸이'].icon}" style="width:36px; left:calc(50% + 30%); transform:translate(-50%, -50%); top:calc(1 / 8 * 100%); border-radius:3px; opacity:${equipment['목걸이'].unequippedRequired == true ? '0.3' : '1' }; background-color:${selectBackground(equipment['목걸이'].grade)}" />
                <img class="position-absolute" src="${equipment['귀걸이1'].icon}" style="width:36px; left:calc(50% + 30%); transform:translate(-50%, -50%); top:calc(2 / 8 * 100%); border-radius:3px; opacity:${equipment['귀걸이1'].unequippedRequired == true ? '0.3' : '1' }; background-color:${selectBackground(equipment['귀걸이1'].grade)}" />
                <img class="position-absolute" src="${equipment['귀걸이2'].icon}" style="width:36px; left:calc(50% + 30%); transform:translate(-50%, -50%); top:calc(3 / 8 * 100%); border-radius:3px; opacity:${equipment['귀걸이2'].unequippedRequired == true ? '0.3' : '1' }; background-color:${selectBackground(equipment['귀걸이2'].grade)}" />
                <img class="position-absolute" src="${equipment['반지1'].icon}" style="width:36px; left:calc(50% + 30%); transform:translate(-50%, -50%); top:calc(4 / 8 * 100%); border-radius:3px; opacity:${equipment['반지1'].unequippedRequired == true ? '0.3' : '1' }; background-color:${selectBackground(equipment['반지1'].grade)}" />
                <img class="position-absolute" src="${equipment['반지2'].icon}" style="width:36px; left:calc(50% + 30%); transform:translate(-50%, -50%); top:calc(5 / 8 * 100%); border-radius:3px; opacity:${equipment['반지2'].unequippedRequired == true ? '0.3' : '1' }; background-color:${selectBackground(equipment['반지2'].grade)}" />
                <img class="position-absolute" src="${equipment['팔찌'].icon}" style="width:36px; left:calc(50% + 30%); transform:translate(-50%, -50%); top:calc(6 / 8 * 100%); border-radius:3px; opacity:${equipment['팔찌'].unequippedRequired == true ? '0.3' : '1' }; background-color:${selectBackground(equipment['팔찌'].grade)}" />
                <img class="position-absolute" src="${equipment['어빌리티 스톤'].icon}" style="width:36px; left:calc(50% + 30%); transform:translate(-50%, -50%); top:calc(7 / 8 * 100%); border-radius:3px; opacity:${equipment['어빌리티 스톤'].unequippedRequired == true ? '0.3' : '1' }; background-color:${selectBackground(equipment['어빌리티 스톤'].grade)}" />
            </div>
        `;
        $('.image-container').html(imgDom);

        // 해제해야 할 장비 표시
        const unequippedStr = Object.entries(equipment)
            .filter(([key, value]) => value.unequippedRequired === true)
            .map(([key, value]) => key)
            .join('/');
        $('.certification-equipment').text(unequippedStr);

        // 인증 버튼 보이도록 설정
        $('.certification-container').removeClass('d-none');
    } else {
        // 캐릭터 이미지 대신 에러 이미지 표시
        const imgDom = `
            <img class="w-100 mb-4" src="/img/sad_loacon${Math.ceil(Math.random() * 4)}.png" style="max-width:550px; max-height:550px;" />
            <p>해당 캐릭터의 이미지를 불러오지 못했습니다..</p>
        `;
        $('.image-container').html(imgDom);

        // 인증 버튼 보이지 않도록 설정
        $('.certification-container').addClass('d-none');
    }
}

// 등급별 배경색 설정
function selectBackground(grade) {
    if(grade === '일반') return '#595959';
    if(grade === '고급') return '#3C5B13';
    if(grade === '희귀') return '#113E5E';
    if(grade === '영웅') return '#4F1464';
    if(grade === '전설') return '#B06900';
    if(grade === '유물') return '#AA4101';
    if(grade === '고대') return '#DFC786';
    if(grade === '에스더') return '#3EF5EB';

    return '';
}

// 캐릭터 인증 상태 초기화 (세션 초기화)
function resetCertificationState() {
    // 1. 과도한 api 요청 방지를 위한 스로틀링
    if(resetTimer) {
        alert('잠시 후 다시 시도해주세요.');
        return;
    }
    
    resetTimer = setTimeout(() => {
        console.log('인증 초기화 가능');
        resetTimer = null;
    }, 5000);

    // 2. 인증 데이터 초기화 요청
    $.ajax({
        url: '/member/certification/reset',
        method: 'DELETE',
        success: function(data) {
            renderDefaultImage();
            setTimeout(() => alert(data), 20);  // 이미지 렌더링 함수 실행을 기다린 후 (0.02초) 메시지 출력
        },
        error: (xhr) => {
            console.error("error: ", xhr);
        }
    });

    // 3. 인증 dom 제거
    $('.certification-container').addClass('d-none');
}