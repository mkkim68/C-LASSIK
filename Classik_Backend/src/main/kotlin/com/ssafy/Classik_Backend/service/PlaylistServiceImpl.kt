package com.ssafy.Classik_Backend.service

import com.ssafy.Classik_Backend.entity.Member
import com.ssafy.Classik_Backend.repository.MemberRepository
import com.ssafy.Classik_Backend.auth.util.getLoginUserId
import com.ssafy.Classik_Backend.dto.*
import com.ssafy.Classik_Backend.entity.Playlist
import com.ssafy.Classik_Backend.entity.PlaylistTrack
import com.ssafy.Classik_Backend.entity.Track
import com.ssafy.Classik_Backend.repository.PlaylistRepository
import com.ssafy.Classik_Backend.repository.PlaylistTrackRepository
import com.ssafy.Classik_Backend.repository.TrackRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PlaylistServiceImpl(
    private val playlistRepository: PlaylistRepository,
    private val playlistTrackRepository: PlaylistTrackRepository,
    private val memberRepository: MemberRepository,
    private val trackRepository: TrackRepository,
    ) : PlaylistService {

    @Transactional
    override fun getPlaylist(playlistId: Int): PlaylistResponseDto {
        val playlistTracks = playlistTrackRepository.findAllByPlaylistId(playlistId)
        val playlist = playlistRepository.findById(playlistId).orElseThrow {IllegalArgumentException("Playlist Not Found. ID: $playlistId")}
        val tracks = ArrayList<PlaylistTrackResponseDto>()
        for (playlistTrackId in playlistTracks) {
            tracks.add(playlistTrackId.toPlaylistTrackDto())
        }
        return PlaylistResponseDto(
            playlistTitle = playlist.title,
            playlistId = playlist.id,
            trackCount = playlistTracks.size,
            tracks = tracks
        )
    }

    @Transactional
    override fun getPlaylists(): List<PlaylistThumbnailResponseDto> {
        val playlistsPerUser = playlistRepository.findByMemberId(getLoginUserId())
        val playlists = ArrayList<PlaylistThumbnailResponseDto>()
        for (playlist in playlistsPerUser) {
            playlists.add(getPlaylistForThumbnail(playlist.id))
        }
        return playlists
    }

    private fun getPlaylistForThumbnail(playlistId: Int): PlaylistThumbnailResponseDto {
        val playlistTracks = playlistTrackRepository.findAllByPlaylistId(playlistId)
        val playlist = playlistRepository.findById(playlistId).orElseThrow {IllegalArgumentException("Playlist Not Found. ID: $playlistId")}
        val thumbnailUrls = ArrayList<String?>()
        for (trackId in playlistTracks.take(3).map { it.track.id }) {
            thumbnailUrls.add(trackRepository.findById(trackId)
                .orElseThrow { IllegalArgumentException("Track Not Found. ID: $trackId") }
                .thumbnailUrl)
        }
        return PlaylistThumbnailResponseDto(
            playlistId = playlist.id,
            playlistTitle =  playlist.title,
            trackCount = playlistTracks.size,
            thumbnailUrls = thumbnailUrls
        )
    }

    @Transactional
    override fun createPlaylist(requestDto: PlaylistCreateRequestDto): PlaylistCreateResponseDto {
        val member: Member = memberRepository.findById(getLoginUserId()).orElseThrow { IllegalArgumentException("User Not Found.") }
        val playlistId = playlistRepository.save(Playlist(title = requestDto.title, member = member)).id
        return PlaylistCreateResponseDto(playlistId)
    }

    @Transactional
    override fun updatePlaylist(playlistId: Int, requestDto: PlaylistUpdateRequestDto): PlaylistResponseDto {
        val playlist: Playlist = playlistRepository.findById(playlistId).orElseThrow { IllegalArgumentException("Playlist Not Found. ID: $playlistId") }
        for (trackId in requestDto.trackIds) {
            val track: Track = trackRepository.findById(trackId).orElseThrow { IllegalArgumentException("Track Not Found. ID: $trackId") }
            playlistTrackRepository.save(PlaylistTrack(playlist = playlist, track = track))
        }
        return getPlaylist(playlistId)
    }

    @Transactional
    override fun deletePlaylist(playlistId: Int) {
        playlistRepository.deleteById(playlistId)
    }

    @Transactional
    override fun deletePlaylistTrack(playlistTracksDeleteRequestDto: PlaylistTracksDeleteRequestDto) {
        for (trackId in playlistTracksDeleteRequestDto.playlistTrackIds) {
            playlistTrackRepository.deleteById(trackId)
        }
    }

}