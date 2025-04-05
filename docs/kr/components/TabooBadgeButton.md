## TabooBadgeButton
`TabooBadgeButton`은 클릭할 수 있는 기본적인 버튼입니다. 
`TabooButton`과 비슷한 디자인을 가지고 있지만, `buttonShape`와 `buttonType`을 지정할 수 없습니다.

![TabooBadgeButton](https://github.com/HanJunKwon/Taboo/blob/feature/readme/docs/assets/buttons/taboo_badge_button.png)


## 사용하기
`TabooBadgeButton`을 사용하기 위해서 다음과 같이 레이아웃 파일에 추가합니다.

### 기본 사용 예제
아래 예제는 버튼의 기본 사용 방법에 대한 예제입니다.

![TabooBadgeButton](https://github.com/HanJunKwon/Taboo/blob/feature/readme/docs/assets/buttons/taboo_badge_button.png)

```xml
<com.kwon.taboo.button.TabooBadgeButton
    android:id="@+id/btn_badge"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="다음"/>
```

## 구성 및 주요 속성

### Text 속성들
| 요소               | 속성                  | 관련 메서드                            | 기본값 |
|------------------|---------------------|-----------------------------------|-----|
| **Text**         | `android:text`      | `setText(String)`<br/>`getText()` | ` ` |

### Button 속성들
| 요소                  | 속성                        | 관련 메서드                                  | 기본값                                    |
|---------------------|---------------------------|-----------------------------------------|----------------------------------------|
| **Primary Color**   | `app:primaryColor`        | `setPrimaryColor`<br/>`getPrimaryColor` | `R.color.taboo_blue_900`        |
| **Ripple Color**    | `app:rippleColor`         | `setRippleColor`<br/>`getRippleColor`   | `R.color.taboo_button_ripple_color`    |

### Badge 속성들
| 요소           | 속성          | 관련 메서드                    | 기본값   |
|--------------|-------------|---------------------------|-------|
| **Badge**    | `app:badge` | `setBadge`<br/>`getBadge` | `0`   |