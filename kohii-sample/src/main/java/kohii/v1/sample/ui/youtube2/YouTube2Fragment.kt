/*
 * Copyright (c) 2019 Nam Nguyen, nam@ene.im
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kohii.v1.sample.ui.youtube2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import kohii.v1.experiments.UnofficialYouTubePlayerEngine
import kohii.v1.sample.R
import kohii.v1.sample.common.BaseFragment
import kohii.v1.sample.common.DemoContainer
import kohii.v1.sample.ui.main.DemoItem
import kotlinx.android.synthetic.main.fragment_recycler_view.recyclerView

class YouTube2Fragment : BaseFragment(), DemoContainer {

  companion object {
    fun newInstance() = YouTube2Fragment()
  }

  private val viewModel: YouTubeViewModel by viewModels()

  override val demoItem: DemoItem? get() = arguments?.getParcelable(KEY_DEMO_ITEM)

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_recycler_view, container, false)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    val engine = UnofficialYouTubePlayerEngine[requireContext()]
    engine.register(this)
        .addBucket(recyclerView)

    val adapter = YouTubeItemsAdapter(engine)
    recyclerView.adapter = adapter

    viewModel.posts.observe(viewLifecycleOwner) {
      adapter.submitList(it)
    }

    viewModel.networkState.observe(viewLifecycleOwner) {
      adapter.setNetworkState(it)
    }

    viewModel.refreshState.observe(viewLifecycleOwner) {
      adapter.setNetworkState(it)
    }

    viewModel.loadPlaylist(YouTubeViewModel.YOUTUBE_PLAYLIST_ID)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    recyclerView.adapter = null
  }
}
