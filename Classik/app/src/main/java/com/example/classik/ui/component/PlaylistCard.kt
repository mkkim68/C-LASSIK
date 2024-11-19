package com.example.classik.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.classik.viewmodel.PlaylistViewModel

@Composable
fun PlaylistCard(
    playlistId: Int,
    title: String,          // 곡 제목
    trackCount: Number,       // 저장된 곡 수
    thumbnails: List<String>,  // 썸네일 리스트
    navController: NavController,
    playlistViewModel: PlaylistViewModel
) {
    val maxImages = 3
    val context = LocalContext.current
    val fetchPlaylist by playlistViewModel.fetchPlaylist.observeAsState()
    Column (
        modifier = Modifier
            .size(width = 210.dp, height = 168.dp)
            .clickable(enabled = true) {
                playlistViewModel.fetchPlaylistById(playlistId, context) {
                    navController.navigate("playlistDetailScreen/${playlistId}")
                }

            },
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .padding(0.dp)
        ) {
            (0 until maxImages).forEach { index ->
                if (index < thumbnails.size) {
                    AsyncImage(
                        model = thumbnails[index],
                        contentDescription = "Thumbnail $index",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(190.dp)
                            .height(120.dp)
                            .offset(x = (index * 10).dp, y = (-index * 10).dp)
                            .zIndex((maxImages - index).toFloat()) // 가장 앞에 있는 이미지가 위로 오도록 설정
                    )

                } else {
                    Box(
                        modifier = Modifier
                            .width(190.dp)
                            .height(120.dp)
                            .offset(x = (index * 10).dp, y = (-index * 10).dp)
                            .background(if (index == 1) Color(0xFFD9D9D9) else Color(0xFFBEBEBE))
                            .zIndex((maxImages - index).toFloat())
                    )
                }
            }
            Text(
                text = "$trackCount 곡",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                    .padding(4.dp)
                    .zIndex(5f)
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 20.sp, // 폰트 크기 줄이기
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            maxLines = 1
        )
    }
}


//@Preview(showBackground = true)
//@Composable
//fun PlaylistCardPreview() {
//    val mockThumbnails = listOf(
//        Thumbnails("data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxMTEhUTExIWFRUXGBoXGBcYFxoYFxgXFxUXFxcXFxcYHSggGB0lHRcYITEhJSkrLi4uFx8zODMtNygtLisBCgoKDg0NDw8PDysZFRkrKysrNystKy0tLSstKy0tNzc3LS03LSsrLSstNzcrKysrNysrKysrKysrLSsrKysrK//AABEIAMABBgMBIgACEQEDEQH/xAAcAAACAgMBAQAAAAAAAAAAAAADBAIFAQYHAAj/xABAEAABAwEEBgcECAYCAwAAAAABAAIDEQQFITEGEkFRYXETIoGRobHBBzJCUhQjYnKC0eHwFTNDU7LxJKIWksL/xAAWAQEBAQAAAAAAAAAAAAAAAAAAAQL/xAAXEQEBAQEAAAAAAAAAAAAAAAAAEQEh/9oADAMBAAIRAxEAPwDrYfVuSSldmpsOCBKVAJyGSiOKG4ooUpwWGbVGV2CzHt5qDFpPVdy9Fo8MlKrdrT7pHA+S0OuLu1BqN62v6x3NV8lrQr0m+sdzVfJLxUU4608VEWhV3SKTZEiLazykorz3qoZP5KJtB3oq3LkJ0x3pBsxRGPqEDbJ+KILTiq7X7lLWQbFcshMlOBWJbT1jjtUNE26034XJS0Hru+8UFm+0YBFknAAx2Kpc4kgItpkxoUD7bSmmT9UqhZKrIO6iBqKZdcukatnjH2R5LjtjxIHJdqs7KRMHAeSuJqZmwyQJH4FEfkl7R7qoTcVVNf8AXAcVYuKqrLjP3qC3nfgOaNbJfq2pa0D3UW8BRrOSBMleQ3nFYRW3xO6qG/FQjdgsjJVliiE9FohPRQJclgbVmQ4LLPVQDmyPJaETi7mVt9931Z7OKSyBriMGjFx7Bkua2jSSIF1GuNSaZBBp16SfWO5lVr3pq3CrnOBzNUg/NMEy5e6RDqsVVDET8VFz6lQjKxVAw1yI15SzDiiAqBhrlIAoUbqIgcTtKK2nQRtbRT7LvRJ2tvXf94+as/ZvGTafwH0SF4s+tk++fNAKJtXoE7qk803Cygc7dgk6qDMdVZvPVCro81ZTHABA3czCZGD7Q8wu4EUa0cFxrRhtZ4x9oea7ZKKUVxNKSNwSVpOCzfF59CB1C7kab/yKop9JgRToXd4VDr25qtuputMTuBKDJfoI/lOSdhvbo3F3RuNRRQbHa82clO9a1aPsqkfpAC4Hond4WbdpBrmoidlTMIGAsqtben2HeC8it1Y7BFiFQhNOCJEqiQQpCihLuOKCEgVTpJfH0WFzxTXJ1WA/Mdp4AY9ytJjgud6fWkunayuDG17XYk91FBrQhfNIXOcSSalzsSVbG5IXADV7VmwRdWqbDw3MgdqKrzoM1+UpG7CqrL30BlYxz2uDw0VwwOGeG1dDu3EYFWraUyQfN5YhrY9NbA2C1ysaKNPXA2Udjh21Wu0VRONYBWWKAVBmZqbShsR4woJMCYiBqi2Gxukc1jBVziABvK2BuhdtBr9HJ5OafVQWXs0H/KP3D6KuvX+fLj/UPmto0CuC0Q2nWkhcxuqRUjDZtVPflzTmaQ9DJQvJB1DTPkil7LdpkYaGmNVEaOv+Ydy2DRmxvDTrMcKHa0rolm0ODmNd0lCRWlMqoOOs0feMapg3LIcqLrEmhL9kje5AdodM2pBae1IVp2iN0vbaIy4Cmt6FdWnwWpaPRfXgUyW32kYq4mqeeMG0xcx/i9X7oGfKO5UhB+kxc/JjvzV9L7pTArIGbh3BLl0dcm9wVFpBbXD6thxOe+h2Jdlmj1Bqv1ZBjWu3ilGz6jPlHcoiJvyjuVRYL5ZqtD662VaYVGCtopAcRiKIgNvhbQdUZ7huK8vXkcBz9CsKKWYMEWNtUGM4IjHb1RB7qYKLVFxxUgoFrwmDGFx2CvdiucXk3p3GV2DnAGmymX5Lo9sjBaQcjgeRWhOsXRudXGnV5UqAUXALJHqtolrXMMRqF2+gqmYyQVl7gDUd3qCFBGzkWeRuq5zSTRzDsyNcMKY7FstpvFzZOjYYw4gHrV25ZbaLWpesdY55DgFsEFiY7o5Hsa40pUitP3VUc09o4/5j6ka2q2oGQOrkP3tWmrYtOItW2ztqSA4AV3ajSB2Ba6VcTRGOWFFqlC2rgFUFhom7OKpmCHdQdisLPD+6LKti9ml3a9p1yMI2k9pwHquvwNWoezyw6kBec3uJ7BgPGq3aAIDxMTEbVmMKcmAJ20w55BVAYWYF28nuGA8lMOIy80w2PADcKKD4wqAdK7ee9E+kP2uKiWdi81QK2awsZMNQUJGse+g9VYWgYrSBp3Cy0yExuc0dQEEV6uFaHea7VZs08sbzi57PvM9W1RYfp/yo/wAX+B/NXMz6NNclrVlvqzyWmMsmjI62bqfCPmori+HjoJKGo1dnFEa3fDQ5xkYQSMw3cNqg62jVLy0FuGwZ7OSrbBaNSQH4cncjgVs1juyjyDQscMtla4HuKiqi6IjmRnwyrkre5w5sdHDHWPdVHjukNNNbDlim5Y6ZKhG830aOfoV5YvUdUc/QryAMRwWHOQWvoFh0m9QSacUYIUYVffl9w2dmtI+lcgMXHkEGb9vSKCPXkdqivMk7gNq53Zb76eaUxtIqdYtJqdXIuHLbRUGmOkP0mbWGDGjVaDnTOp4krX7HeD4pGyRu1XtNWnjuO8HIjig6Y01Ky9m7EpCy29skYnaKNJ1XtH9KTa0/ZObTu5I8ji7Bri07xu4IozbvkeQGP1Tty/dFtVhjdHH9Y4EAEkgUpQVWsXRBI19S+Wm9oB7xRN6X3ubPZZA51XSt1IhSjhUEOJpsANe4IOT3zbDNLJK7N7i7vyHYKDsVWiyFCoriPBM2JvWHBL0TthZt34JqLWzqxsza4DM+uSTs7Ctq0Nu/pLSyowb1z+HLxostOl3PZujjZGB7rQO4Yq4hagQRqxhjWkEiRSKlo417B+tFlkanEyrjww9T6KokDwQnFMOZTYgPHFABzkhf1u6Gzyy191hpzpQeJVi5vFaH7V7x1IY4AcZHVd91v60U0c6bLQefasOkCAXLAKjRlhqQBt812u7bHq2FsY+T/S5PondDrRO1gNACCXZ0AxwG/BdyjiAaANgp3Jia03+Du3LZboaQwA/DgDwThhXmsI3KxEXmiADXapyAnDBRbHRAlevujn6FeUr390c/QrCKq1AlZBwVTfd8Ns8bnu5NHzO2BQHvu+WWeMveeDW7XHguN31e755S95qTs2NGwDgsX3fD53l8jsdg2AbgNgVFLJigdmiDhlXzVXJGWuR2T7k41ofgaVGX5HggY0SvkWebrjWhkGpK3YW7+bTj3rfrZcjozrwP14ziGnMDgfiHitDgscda6tF2jQi1RvsYc8NAjBa4kCgDRWp7EGs2LSIxNq9jjs6tDXsWkaVWye1SmRzC1owa35W7O3erTT2+Na1FzGajNUalMAWgnHDbWq1d14OdWrqUCKQmsj9yBqJuS0OOZQweX75qoFGyporWzRcOSHZmA4UpxCfjYBtUDdlYul+zm76MfKR7x1Rybn4nwXO7GwkgChJNABvOFF3C4Lu6KFkdPdaK88z4pirCzxc1YRtQoGJ2MLTLLRgTuWYo6AbzieZxKIW5Df5DE+iIWIAuw3oDim3jihPaUCp7FwzT+9untr6Hqs6jfw5+K67phef0ayySH3qUb944BfPT31JJxU1cM6yy0pZrlJruKyrpXs2tEcLTLKaBxcAaE+6G7hzW/N0nstP5v/V35LiF624ssVk1CQS6fWo4t+NtMuCqo77PxdN2Sn1CtR9C/wDkVm/ujuP5L38fs395vj+S4Ky+4sNaa0t7Q71TkN6wEU+myNJ+ZpHolI7Y6/LN/eZ3rIvmz/3md647AI3+7eTTze0HudRPx3HORVtr1uRYfJKR0e87zhLRSVhx2EbivLl1vu61RAEzEg4ZBeQjfHvwXKNOb46WUtB6jOqOfxH97lv2k949DZ3OB6x6reZ29gqVxa3SEkoFZpKpYuRHIRCoyySmYwTMExBqDUePagRgOw27OPBeiz2g70RsNnlDqEK/sVpJs1os5eWB7NcHe6I64aeDgCO5afA7VPW6p2OGR5hW8DgQWk/6OY8fEKNI3ix01kjkGJY6lduqQR5gKhbZn/Ke5Xl1TkWe0wn3mfWN5BwDvIHtKRZeBGwHwRCQjpgQUSKMA1IqNxqK9oVmy3sPvNI8VMCI5UHeEEIzDsipyeT5rwj3OI5owsDTiD5FT+hEZHxI/NFCszng1biRjUDEU24ZUWzXZpjbGU1ZXuA39b/IFa6Y3j9g+Sas1uezaRswJCg6HYfaNaWgGSz6zd+qW+OSvbH7UrOcJInt5EH8ly0Xw5wo5z6cwQhi0MccXDtBHjirSO52HTqwvP8AO1dg1gRzV7Zr3s8nuTxnk8fmvnJkLTkeVHNPgaFEksr2gEA5Y1a4U4VFQeaVI+lBjkaocuS+cbNetojPUle0/Zf+qs4dP7wj/qucPtt1h3kK0i49sV968rLMDgwazuZy8FzNu1M3vehnlfJJi95qSDt4DYq02gDCpA4hQNhyyJKJZkw+YeSYAr7px2DDzqorN5Sl9mAGPRSV/DIAK/8As0d6oOlPJbNZrptLj1WVqKGtKFpzBG1NWnQM1OpLQbnCviKKjUvpJ3lZ+l+W4ZrZ3aDOp/Nx+7+qrLTonO3Itd3j0VRVMtANKgc8cOxFskza4Aj8SjLck7c4yeRBQhZnDNjgeRQWkNtcRTppabqkj/JeSELeBXlFb7p5eGsQwHBox+8f2Fz6V+au9ILXrucTtJK14piai4KBCIVCqqIURGurngd+/mvNG9HZZq7UU5ZH/C8YHJWEMOqQNnof3XsVdZmObhSo2hW8Mes0UrUbDu2jiooLrMRKSMnxPYab9U/kFTsC2yJ2ANKmlO3Iqgng1ZHN4nuzCADY0WONGY1GYwKDEMXZyTcevvPbipQtTTY0Adc7geWCmJhtBHZVEDApdCEA2iM7W+S86xg5V81h7W7Qk5MMiRywQMOu87COVFhkczMWkj7rqIDbc8ba88VJt7729xQHN5TD3jrcHsDvEiqau3SDoiXfRoXHi2tOQdUDuSf8SYd45/ovFzHbGlASe8GOcX1kjcdwa5vdglLS5r8deJ5x95pjccNpbmoTMGz996Sls5xo7v8A0VBhZo8iDhtY8HuDh6oYDYzVpDscnt86YFLGyvrv5FDfrNzqOYKDbrDp1JF1XQxuHCrT6hW9m9oVnP8AMge3iCHedFzWedxzJPM180HXO9Ijq9r07sjWB0bXPdUAsI1MMamtCP8AaJYtLbDM0lzXRkbHFuOFagjPuXJ2lbhopQQSZCr86Y+6NqK2S039ZPgs5k49cjwFFXTX8PgsbAN5YK/9nKvtVqrQB2ZxxSkkqB6W9pXGvRxt7vRq8q9hqvKCnvEqtKftqrwriMubVCLSilRKoxGm4X0SgTMDa4bU0W13yhzqGg3KzEB3jxWvWc0K2G77RWgKim7M3Cir76jo9rqe8PFv+wrpjUG8rIHspkQa18/NQa+W4ZFSjFEZt3vJo3E7gnbPcFod/TI4uw88UAInJhr02zRm0UqSwc3fog2m7ZI9zxvbXyKDAcMNiOwgVIINRTccVXdMvdJVASdhGYPDikJQmtcnbhuS0gQBcEEjFHIQy1EBLd6g9HdihYKqE2Rw+IrP0lwzoVh6g8IGYrcNoI5Jhtsafi71WsZVYc1BZSMY74Wmv72JOeys4inb5pN7cVF0zqEVwREWybfRbBcNsq10RH2gR2Ch8FrjQrC6X0eTwV0X9qo0kbd/YgNlqounbjhjzUYps+ayplpC8oMevIKW1JMtVza7NtVY9iqAuWCVJwWA1UYARInEFRAUgUDwGsNYZjMJizT6pzSdldqmqb6EHrN7QorZLvtQcM6FGtloDG4gkO6uGzDBayyQjarWxWwvGq4Bw3FQN3RE+N7Za1bjWmJxFMltsN5spi4FUcBwpSgpQDcmY4mubRzQSeCC5+mNd7rj2AFVt62S0lutG6R1TQNEdO3WpRDscnQ16M6h4FPw6UFho9usMsCQQOAOHkqNW/g1oe41iJdmcgBzOVU9DoVaXCtGDhrY+Aouh2KVkrWvYatOXqOBTkdmNDu4kojkV7aNTwCrgCKYlp1qc8FRuYu2y2V5rTojTOoND4rSdKrtbGKmGNrnGmsx2VM6toKBFaIWqOrinpICdmHglnR0KgBIEFwTEoQXgohdw3qLkQoTlVeDlF7l4FecEAnEoZYiSlYDq5IjzIahM2eEtqSitcABgsdNhkggCUaAFBMyJDIEU61eUY3rCgnaXKqlKsbScFWSFVAysBYNV5pVGQsBSOS81QFiKbgl1TUJRqkHkIq0oHYhFsxLTUKvim40KcitKg2KC11oE/A8jErW4J6FOTXjq7MUDt5WwA1rjTxVa2YkJCScuOJWDIg3HR3SgWaNzXtJx1m03kYjwqpR6eT9KJKMLR/TcKt5nGtVp7pcM6IPScUHbbq9qlmkoJojGcKkUc3jxW32G3WO0j6qSN9dmFe1pxXzOLQNoB45FGjmoaseWnjh4hWpH0Pfmh1ntFOkZ7oNKEtpXgMCtJvb2WBrHGN7nyfCH0Y3PaQDsWq3V7QLdZwPrC9o2O64pz2Lc7p9sMLuraISz7TDUdx/NXg5/fWhlpgYXOjcaZloqwN36+3E0pRa5LYnga2q4D5qGnfRfSl3aQWG1D6qdhJ+EnVd3HNevrReG0MDZW67QagVIxpSuBUhXy7KOKBIu6W/2T2cvLtaRra4MaW6o4A0JWgX97PbXDVwjD21IDYyXupiQTgOSDR2BecUzaLulY4sexweM20JI7kqYzv9UUCVHsIFauI4KJjrsJ5BZjso+J7YwN/WPINbt50RD7wKF2YHrkkHOXjNhqAnVBrjv3lLyvQFB4ojXJIORo3pBaWfJeQbPJgvIGZjmkJGqwclnRk7ECmpREbCDwUiwlGghrwQBFmcMsVgtG6idhJBonoWB2YRVKyGuRHkpPsrxm09mPkruS7GEZdygInMwrUbEFLqkKTK71YTzOrQZbUAxHcEEWuNM0QzFebEUXo+ARAmPUzKjxxDJGMGGQUUow17ViRqsIoqKD46oK+qw4pt9lOdEDoURBkhGRos/SvmAPn4KL40u5qB5swzDi3n+YV/c+m1us1OjmcW/K467e45LUaKcbTXA48FVdkuX2yZNtMAO90Zp26pz71u92abXfaQA2ZgcSOrINU+OHivmnpt9D596y2QbCQeKVHSfbTfJZaWQQ0azog52qfeLicyNwHiuSFWVpc4jHrYZ1qq1BKSdzgA5xIApidiGVNkZccB+iZhuuR3utJG/IDtOCBAqJCsnWKNvvyt5MGue8Ub4oMk0Y92OvF5/wDltB4lAo1lchjuzTEdmI96jeZx7hio/SXHCtBuaA0dwUMlRYMlYMKk8hTxK8kmLyg//9k="),
////        Thumbnails("data:image/jpeg;base64,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"),
////        Thumbnails("data:image/jpeg;base64,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"),
//    )
//    PlaylistCard(
//        title = "출근길 플리",
//        trackCount = 23,
//        thumbnails = mockThumbnails
//    )
//}